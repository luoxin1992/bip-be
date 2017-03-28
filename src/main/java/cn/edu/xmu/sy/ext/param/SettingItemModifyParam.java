/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 修改设置项Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingItemModifyParam extends BaseParam {
    /**
     * ID
     */
    private Long id;
    /**
     * 配置键
     */
    private String propKey;
    /**
     * 配置值
     */
    private String propValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
}
