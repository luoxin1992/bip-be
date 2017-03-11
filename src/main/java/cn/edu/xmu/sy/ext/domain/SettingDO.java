/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.domain.BaseDO;

/**
 * 设置Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class SettingDO extends BaseDO {
    /**
     * 类型
     */
    private String type;
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
