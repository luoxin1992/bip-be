/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

/**
 * 设置Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class SettingDO extends BaseDO {
    /**
     * 父类ID
     */
    @FieldComment("父类ID")
    private Long parent;
    /**
     * 属性名
     */
    @FieldComment("属性名")
    private String propName;
    /**
     * 属性值
     */
    @FieldComment("属性值")
    private String propValue;
    /**
     * 描述
     */
    @FieldComment("描述")
    private String description;

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
