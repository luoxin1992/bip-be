/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 保存设置项Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingItemSaveParam extends BaseParam {
    /**
     * ID
     */
    @NotNull
    @Min(1)
    private Long id;
    /**
     * 键
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String key;
    /**
     * 值
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
