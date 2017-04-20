/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存设置Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingSaveParam extends BaseParam {
    /**
     * 设置项
     */
    @NotNull
    private List<SettingItemSaveParam> items;

    public List<SettingItemSaveParam> getItems() {
        return items;
    }

    public void setItems(List<SettingItemSaveParam> items) {
        this.items = items;
    }
}
