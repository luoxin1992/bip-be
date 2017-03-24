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
     * 类别
     */
    @FieldComment("类别")
    private String category;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
