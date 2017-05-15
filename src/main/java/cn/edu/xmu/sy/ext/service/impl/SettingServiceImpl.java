/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.SettingDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SettingMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.SettingItemSaveParam;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingGroupListResult;
import cn.edu.xmu.sy.ext.result.SettingItemListResult;
import cn.edu.xmu.sy.ext.result.SettingListResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-27
 */
@Service
public class SettingServiceImpl implements SettingService {
    private final Logger logger = LoggerFactory.getLogger(SettingServiceImpl.class);

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LogService logService;

    @Autowired
    private SettingMapper settingMapper;

    @Override
    @Transactional
    public void save(SettingSaveParam param) {
        //找出被修改的设置项，未修改的不需要更新
        List<SettingItemSaveParam> modifies = filterModifyItems(param.getItems());
        if (CollectionUtils.isEmpty(modifies)) {
            logger.error("setting not modify");
            throw new BizException(BizResultEnum.SETTING_NOT_MODIFY);
        }

        modifies.forEach((item) -> {
            SettingDO domain = settingMapper.getById(item.getId());
            validateItem(item.getValue(), domain.getRegExp());
            saveItem(domain.getId(), domain.getDescription(), domain.getValue(), item.getValue());
        });
        logger.info("{} setting(s) modify", modifies.size());

        //检查是否修改了TTS相关设置，若修改了，需要重新合成所有语音资源
        checkTtsSettingModify(modifies);
    }

    @Override
    public SettingListResult list() {
        List<SettingDO> domains = settingMapper.list();
        if (CollectionUtils.isEmpty(domains)) {
            logger.error("setting list result is empty");
            throw new BizException(BizResultEnum.SETTING_NOT_EXIST);
        }

        //过滤出设置项和设置组，再分组
        Map<Long, List<SettingItemListResult>> items = domains.stream()
                .filter(setting -> !setting.getParent().equals(0L))
                .collect(Collectors.groupingBy(SettingDO::getParent,
                        Collectors.mapping(setting -> POJOConvertUtil.convert(setting, SettingItemListResult.class),
                                Collectors.toList())));

        List<SettingGroupListResult> groups = domains.stream()
                .filter(setting -> setting.getParent().equals(0L))
                .map(setting -> POJOConvertUtil.convert(setting, SettingGroupListResult.class))
                .peek(setting -> setting.setItems(items.get(setting.getId())))
                .collect(Collectors.toList());
        logger.info("list {} setting item(s) in {} group(s)", domains.size() - groups.size(), groups.size());

        SettingListResult result = new SettingListResult();
        result.setGroups(groups);
        return result;
    }

    @Override
    public Optional<String> getValueByKeyOptional(String key) {
        String value = settingMapper.getValueByKey(key);
        return Optional.ofNullable(value);
    }

    /**
     * 对比旧的配置，找出被修改的配置项
     *
     * @param items 新的配置项
     * @return 被修改的配置项
     */
    private List<SettingItemSaveParam> filterModifyItems(List<SettingItemSaveParam> items) {
        return items.stream()
                .filter((item) -> {
                    String value = settingMapper.getValueByKey(item.getKey());
                    return !(StringUtils.isEmpty(value) || value.equals(item.getValue()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据校验正则验证被修改的设置，取值是否合法
     *
     * @param value  设置值
     * @param regExp 校验正则
     */
    private void validateItem(String value, String regExp) {
        if (!StringUtils.isEmpty(regExp) && !value.matches(regExp)) {
            logger.error("setting value {} mismatch regular expression {}", value, regExp);
            throw new BizException(BizResultEnum.SETTING_VALIDATE_FAIL);
        }
    }

    /**
     * 保存被修改的设置
     *
     * @param id          设置ID
     * @param description 描述
     * @param from        原值
     * @param to          新值
     */
    private void saveItem(Long id, String description, String from, String to) {
        SettingDO domain = new SettingDO();
        domain.setId(id);
        domain.setValue(to);

        if (settingMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("save setting {} failed", id);
            throw new BizException(BizResultEnum.SETTING_SAVE_ERROR);
        }

        logService.logSettingSave(description, from, to);
        logger.info("save setting {} successful", id);
    }

    /**
     * 检查是否修改了TTS相关的设置
     *
     * @param items 被修改的设置
     */
    private void checkTtsSettingModify(List<SettingItemSaveParam> items) {
        boolean modify = items.stream()
                .anyMatch(item -> item.getKey().startsWith(SettingEnum.TTS.getKey()));
        if (modify) {
            logger.info("rebuild all voice resource because of tts setting(s) modify");
            resourceService.rebuildAllVoice();
        }
    }
}
