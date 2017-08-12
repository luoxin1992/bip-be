/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.param.BaseListParam;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.SettingQueryParam;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
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
    void save(BaseListParam<SettingSaveParam> param);

    /**
     * 查询全部设置
     *
     * @return 查询结果
     */
    BaseListResult<SettingQueryResult> query(SettingQueryParam param);

    /**
     * 查询设置参数
     * 若查询当前值失败，则返回默认值
     *
     * @param setting 设置
     * @return 查询结果
     */
    String getValueByKeyOrDefault(SettingEnum setting);
}
