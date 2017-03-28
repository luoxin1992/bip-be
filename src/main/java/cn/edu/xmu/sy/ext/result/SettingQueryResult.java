/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 查询设置Result
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingQueryResult extends BaseResult {
    /**
     * 设置组
     */
    private List<SettingGroupQueryResult> settings;

    public List<SettingGroupQueryResult> getSettings() {
        return settings;
    }

    public void setSettings(List<SettingGroupQueryResult> settings) {
        this.settings = settings;
    }
}
