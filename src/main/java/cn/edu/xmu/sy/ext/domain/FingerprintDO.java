/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.CompareIgnore;
import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

import java.time.LocalDateTime;

/**
 * 指纹Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class FingerprintDO extends BaseDO {
    /**
     * 用户ID
     */
    @FieldComment("用户ID")
    private Long userId;
    /**
     * UID
     */
    @FieldComment("UID")
    private Integer uid;
    /**
     * 手指
     */
    @FieldComment("手指")
    private Integer finger;
    /**
     * 模板
     */
    @FieldComment("模板")
    private String template;
    /**
     * 登记时间
     */
    @FieldComment("登记时间")
    @CompareIgnore
    private LocalDateTime enrollTime;
    /**
     * 最后辨识时间
     */
    @FieldComment("最后辨识时间")
    @CompareIgnore
    private LocalDateTime identifyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getFinger() {
        return finger;
    }

    public void setFinger(Integer finger) {
        this.finger = finger;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public LocalDateTime getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(LocalDateTime enrollTime) {
        this.enrollTime = enrollTime;
    }

    public LocalDateTime getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(LocalDateTime identifyTime) {
        this.identifyTime = identifyTime;
    }
}
