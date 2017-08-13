/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 查询设置Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingQueryParam extends BaseParam {
    /**
     * 分组名称
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String parent;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
