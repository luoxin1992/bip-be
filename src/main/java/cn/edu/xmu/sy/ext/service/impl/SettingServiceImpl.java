/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.param.BaseListParam;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.SettingDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SettingMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.SettingQueryParam;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.ResourceQuerySimpleResult;
import cn.edu.xmu.sy.ext.result.SettingQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.TtsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-27
 */
@CatTransaction
@Service
public class SettingServiceImpl implements SettingService {
    private final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private TtsService ttsService;
    @Autowired
    private LogService logService;

    @Autowired
    private SettingMapper settingMapper;

    @Override
    public BaseListResult<SettingQueryResult> query(SettingQueryParam param) {
        List<SettingDO> domains = settingMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.error("setting with parent {} query result is empty", param.getParent());
            throw new BizException(BizResultEnum.SETTING_NOT_EXIST, param.getParent());
        }

        List<SettingQueryResult> results = domains.stream()
                .map(domain -> POJOConvertUtil.convert(domain, SettingQueryResult.class))
                .collect(Collectors.toList());
        logger.info("list {} setting(s) with parent {}", results.size(), param.getParent());

        BaseListResult<SettingQueryResult> result = new BaseListResult<>();
        result.setTotal(results.size());
        result.setList(results);
        return result;
    }

    @Override
    @Transactional
    public void save(BaseListParam<SettingSaveParam> param) {
        //过滤未被修改的设置
        List<SettingSaveParam> items = filter(param.getList());
        if (CollectionUtils.isEmpty(items)) {
            logger.warn("no setting are modified");
            throw new BizException(BizResultEnum.SETTING_NOT_MODIFY);
        }

        //校验和保存设置
        items.forEach(item -> {
            SettingDO domain = settingMapper.getByKey(item.getKey());
            validate(item.getValue(), domain.getRegExp(), domain.getDescription());
            save(domain.getId(), domain.getValue(), item.getValue(), domain.getDescription());
        });
        logger.info("save {} setting(s)", items.size());

        //检查是否修改了公司信息设置和语音合成设置
        postponeWhenCompanyModify(items);
        postponeWhenTtsModify(items);
    }

    @Override
    public String getValueByKeyOrDefault(SettingEnum setting) {
        SettingDO domain = settingMapper.getByKey(setting.getKey());
        return domain != null ? domain.getValue() : setting.getDefaultValue();
    }

    /**
     * 对比旧的配置，找出被修改的配置项
     *
     * @param params 新的配置项
     * @return 被修改的配置项
     */
    private List<SettingSaveParam> filter(List<SettingSaveParam> params) {
        return params.stream()
                .filter(param -> {
                    SettingDO domain = settingMapper.getByKey(param.getKey());
                    //过滤掉key无效/分组/value未被修改
                    return !(domain == null || domain.getParentId() == 0
                            || Objects.equals(domain.getValue(), param.getValue()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据校验正则验证被修改的设置，取值是否合法
     *
     * @param value       值
     * @param regExp      校验正则
     * @param description 描述
     */
    private void validate(String value, String regExp, String description) {
        if (!StringUtils.isEmpty(regExp) && !value.matches(regExp)) {
            logger.warn("setting value {} mismatch regular expression {}", value, regExp);
            throw new BizException(BizResultEnum.SETTING_VALUE_INVALID, description);
        }
    }

    /**
     * 保存被修改的设置
     *
     * @param id          ID
     * @param from        原值
     * @param to          新值
     * @param description 描述
     */
    private void save(Long id, String from, String to, String description) {
        SettingDO domain = new SettingDO();
        domain.setId(id);
        domain.setValue(to);

        if (settingMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("save setting {} failed", id);
            throw new BizException(BizResultEnum.SETTING_SAVE_ERROR);
        }

        logService.logSettingSave(description, from, to);
        logger.info("save setting {}", id);
    }

    /**
     * 公司信息设置被修改后处理
     *
     * @param params 被修改的设置项
     */
    private void postponeWhenCompanyModify(List<SettingSaveParam> params) {
        boolean modify = params.stream()
                .anyMatch(param -> param.getKey().startsWith(SettingEnum.COMPANY.getKey()));
        if (modify) {
            //向所有在线会话发送更新公司信息消息
            Optional<String> logo = params.stream()
                    .filter(param -> SettingEnum.COMPANY_LOGO.getKey().equals(param.getKey()))
                    .map(SettingSaveParam::getValue)
                    .findFirst();
            Optional<String> name = params.stream()
                    .filter(param -> SettingEnum.COMPANY_NAME.getKey().equals(param.getKey()))
                    .map(SettingSaveParam::getValue)
                    .findFirst();
            messageService.sendUpdateCompanyInfo(logo.orElse(null), name.orElse(null));
        }
    }

    /**
     * 语音合成设置被修改后处理
     *
     * @param params 被修改的设置项
     */
    private void postponeWhenTtsModify(List<SettingSaveParam> params) {
        boolean modify = params.stream()
                .anyMatch(param -> param.getKey().startsWith(SettingEnum.TTS.getKey()));
        if (modify) {
            //重新合成所有语音资源
            List<ResourceQuerySimpleResult> results = resourceService.queryByType(ResourceTypeEnum.VOICE.getType());

            List<String> contents = results.stream()
                    .map(ResourceQuerySimpleResult::getTag)
                    .collect(Collectors.toList());
            ttsService.ttsBatchAsync(contents, true);
        }
    }
}
