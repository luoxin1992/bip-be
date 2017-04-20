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
import cn.edu.xmu.sy.ext.param.SettingItemSaveParam;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingGroupListResult;
import cn.edu.xmu.sy.ext.result.SettingItemListResult;
import cn.edu.xmu.sy.ext.result.SettingListResult;
import cn.edu.xmu.sy.ext.service.LogService;
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
    private LogService logService;

    @Autowired
    private SettingMapper settingMapper;

    @Override
    @Transactional
    public void save(SettingSaveParam param) {
        //找出被修改的设置项，未修改的不需要更新
        List<SettingItemSaveParam> modifies = buildModifyItems(param.getItems());
        if (CollectionUtils.isEmpty(modifies)) {
            logger.warn("no setting has been modified");
            throw new BizException(BizResultEnum.SETTING_NOT_MODIFY);
        }
        modifies.stream()
                .map((modify) -> POJOConvertUtil.convert(modify, SettingDO.class))
                .forEach((domain) -> {
                    SettingDO before = settingMapper.getById(domain.getId());
                    checkModifyItem(domain.getValue(), before.getRegExp());
                    if (settingMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
                        logger.error("save setting {} failed", domain.getId());
                        throw new BizException(BizResultEnum.SETTING_SAVE_ERROR);
                    }
                    logService.logSettingSave(before.getDescription(), before.getValue(), domain.getValue());
                    logger.info("save setting {} successful", domain.getId());
                });
        logger.info("{} setting(s) has been modified", modifies.size());
    }

    @Override
    public SettingListResult list() {
        List<SettingDO> settings = querySettings();

        List<SettingGroupListResult> groups = buildGroups(settings);
        if (!CollectionUtils.isEmpty(groups)) {
            //组合设置项到设置组中
            Map<Long, List<SettingItemListResult>> items = buildItems(settings);
            groups.forEach(group -> group.setItems(items.get(group.getId())));
            logger.info("list {} setting item(s) in {} group(s)", items.size(), groups.size());
        }

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
     * 查询所有设置
     *
     * @return 设置组+设置项
     */
    private List<SettingDO> querySettings() {
        List<SettingDO> settings = settingMapper.list();
        if (CollectionUtils.isEmpty(settings)) {
            logger.error("setting list result is empty");
            throw new BizException(BizResultEnum.SETTING_NOT_EXIST);
        }
        return settings;
    }

    /**
     * 从查询结果中过滤出设置组(parent == 0)
     *
     * @param settings 所有设置
     * @return 设置组
     */
    private List<SettingGroupListResult> buildGroups(List<SettingDO> settings) {
        return settings.stream()
                .filter(setting -> setting.getParent().equals(0L))
                .map(setting -> POJOConvertUtil.convert(setting, SettingGroupListResult.class))
                .collect(Collectors.toList());
    }

    /**
     * 从查询结果中过滤出设置项(parent != 0)，并按parent分组
     *
     * @param settings 所有设置
     * @return 设置项
     */
    private Map<Long, List<SettingItemListResult>> buildItems(List<SettingDO> settings) {
        return settings.stream()
                .filter(setting -> !setting.getParent().equals(0L))
                .collect(Collectors.groupingBy(SettingDO::getParent,
                        Collectors.mapping(setting -> POJOConvertUtil.convert(setting, SettingItemListResult.class),
                                Collectors.toList())));
    }

    /**
     * 对比旧的配置找出被修改的配置项
     *
     * @param param 新的配置项
     * @return 被修改的配置项
     */
    private List<SettingItemSaveParam> buildModifyItems(List<SettingItemSaveParam> param) {
        return param.stream()
                .filter((item) -> {
                    String value = settingMapper.getValueByKey(item.getKey());
                    return !(StringUtils.isEmpty(value) || value.equals(item.getValue()));
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据校验正则验证被修改的配置项取值是否合法
     *
     * @param value  值
     * @param regExp 校验正则
     */
    private void checkModifyItem(String value, String regExp) {
        if (!StringUtils.isEmpty(regExp) && !value.matches(regExp)) {
            logger.error("setting value {} validate failed", value);
            throw new BizException(BizResultEnum.SETTING_VALIDATE_ERROR);
        }
    }
}
