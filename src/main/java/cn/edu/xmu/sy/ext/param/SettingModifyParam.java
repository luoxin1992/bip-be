/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改设置Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingModifyParam extends BaseParam {
    /**
     * 设置项
     */
    @NotNull
    private List<SettingItemModifyParam> settings;

    public List<SettingItemModifyParam> getSettings() {
        return settings;
    }

    public void setSettings(List<SettingItemModifyParam> settings) {
        this.settings = settings;
    }
}
