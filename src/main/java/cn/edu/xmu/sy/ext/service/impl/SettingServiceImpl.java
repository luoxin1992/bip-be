/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.SettingDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SettingMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.SettingItemModifyParam;
import cn.edu.xmu.sy.ext.param.SettingModifyParam;
import cn.edu.xmu.sy.ext.result.SettingGroupQueryResult;
import cn.edu.xmu.sy.ext.result.SettingItemQueryResult;
import cn.edu.xmu.sy.ext.result.SettingQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public void modify(SettingModifyParam param) {
        //找出被修改的设置项，未修改的不需要更新
        List<SettingItemModifyParam> modifies = buildModifyItems(param.getSettings());
        if (CollectionUtils.isEmpty(modifies)) {
            logger.warn("no setting has been modified");
            throw new BizException(BizResultEnum.SETTING_NOT_MODIFY);
        }

        for (SettingItemModifyParam modify : modifies) {
            SettingDO domain = POJOConvertUtil.convert(modify, SettingDO.class);
            if (settingMapper.updateById(domain) != 1) {
                logger.error("error modifying setting {}", domain.getId());
                throw new BizException(BizResultEnum.SETTING_MODIFY_ERROR);
            }
        }
        logService.logSettingModify();
    }

    @Override
    public SettingQueryResult query() {
        Map<Long, SettingGroupQueryResult> groups = buildGroups();
        Map<Long, List<SettingItemQueryResult>> items = buildItems();
        for (SettingGroupQueryResult group : groups.values()) {
            group.setItems(items.get(group.getId()));
        }

        SettingQueryResult result = new SettingQueryResult();
        result.setSettings(new ArrayList<>(groups.values()));
        return result;
    }

    @Override
    public String getValueByKey(String key) {
        String value = settingMapper.getValueByKey(key);
        if (value == null) {
            logger.error("setting item {} not exist", key);
            throw new BizException(BizResultEnum.SETTING_KEY_NOT_EXIST, key);
        }
        return value;
    }

    /**
     * 查询设置组
     *
     * @return 设置组
     */
    private List<SettingDO> queryGroups() {
        List<SettingDO> groups = settingMapper.listGroup();
        if (CollectionUtils.isEmpty(groups)) {
            logger.warn("setting group query result is empty");
            return Collections.emptyList();
        }
        logger.info("query {} setting groups", groups.size());
        return groups;
    }

    /**
     * 构造ID-设置组的Map
     *
     * @return ID-设置组的Map
     */
    private Map<Long, SettingGroupQueryResult> buildGroups() {
        List<SettingDO> groups = queryGroups();

        Map<Long, SettingGroupQueryResult> results = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(groups)) {
            //无设置组，所有设置项将不分组，合并展示在默认分组(ID==-1)中
            SettingGroupQueryResult result = new SettingGroupQueryResult();
            result.setId(-1L);
            result.setDescription("默认分组");
            result.setItems(new ArrayList<>());

            logger.warn("build default group");
            results.put(result.getId(), result);
            return results;
        }

        for (SettingDO group : groups) {
            SettingGroupQueryResult result = POJOConvertUtil.convert(group, SettingGroupQueryResult.class);
            results.put(group.getId(), result);
        }
        return results;
    }

    /**
     * 查询设置项
     *
     * @return 设置项
     */
    private List<SettingDO> queryItems() {
        List<SettingDO> items = settingMapper.listItem();
        if (CollectionUtils.isEmpty(items)) {
            logger.warn("setting item query result is empty");
            throw new BizException(BizResultEnum.SETTING_NOT_EXIST);
        }
        logger.info("query {} setting items", items.size());
        return items;
    }

    /**
     * 构造设置组ID-设置项的Map
     *
     * @return 设置组ID-设置项的Map
     */
    private Map<Long, List<SettingItemQueryResult>> buildItems() {
        List<SettingDO> items = queryItems();

        Map<Long, List<SettingItemQueryResult>> results = new LinkedHashMap<>();
        for (SettingDO item : items) {
            if (!results.containsKey(item.getParent())) {
                results.put(item.getParent(), new ArrayList<>());
            }
            SettingItemQueryResult result = POJOConvertUtil.convert(item, SettingItemQueryResult.class);
            results.get(item.getParent()).add(result);
        }
        return results;
    }

    /**
     * 对比旧的配置找出被修改的配置项
     *
     * @param newItems 新的配置项
     * @return 被修改的配置项
     */
    private List<SettingItemModifyParam> buildModifyItems(List<SettingItemModifyParam> newItems) {
        List<SettingDO> oldItems = queryItems();

        List<SettingItemModifyParam> results = new ArrayList<>();
        for (SettingItemModifyParam newItem : newItems) {
            for (SettingDO oldItem : oldItems) {
                if (Objects.equals(newItem.getId(), oldItem.getId())) {
                    if (!Objects.equals(newItem.getPropValue(), oldItem.getPropValue())) {
                        results.add(newItem);
                    }
                    break;
                }
            }
        }
        logger.info("{} setting(s) has been modified", results.size());
        return results;
    }
}
