/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingListResult;

import java.util.Optional;

/**
 * 设置Service
 *
 * @author luoxin
 * @version 2017-3-27
 */
public interface SettingService {
    /**
     * 保存设置
     *
     * @param param 设置参数
     */
    void save(SettingSaveParam param);

    /**
     * 查询全部设置
     *
     * @return 查询结果
     */
    SettingListResult list();

    /**
     * 查询设置参数项
     *
     * @param key 键
     * @return 查询结果
     */
    Optional<String> getValueByKeyOptional(String key);
}
