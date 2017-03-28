/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.SettingModifyParam;
import cn.edu.xmu.sy.ext.result.SettingQueryResult;

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
    void modify(SettingModifyParam param);

    /**
     * 查询设置
     *
     * @return 查询结果
     */
    SettingQueryResult query();

    /**
     * 查询设置项
     *
     * @param key 配置键
     * @return 查询结果
     */
    String getValueByKey(String key);
}
