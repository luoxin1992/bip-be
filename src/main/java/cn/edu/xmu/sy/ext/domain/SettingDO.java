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
     * 父ID
     */
    @FieldComment("父ID")
    private Long parentId;
    /**
     * 键
     */
    @FieldComment("键")
    private String key;
    /**
     * 值
     */
    @FieldComment("值")
    private String value;
    /**
     * 校验正则
     */
    @FieldComment("校验正则")
    private String regExp;
    /**
     * 描述
     */
    @FieldComment("描述")
    private String description;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getRegExp() {
        return regExp;
    }

    public void setRegExp(String regExp) {
        this.regExp = regExp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
