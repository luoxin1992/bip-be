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
     * 手指名称
     */
    @FieldComment("手指名称")
    private String finger;
    /**
     * UUID
     */
    @FieldComment("UUID")
    private String uuid;
    /**
     * 指纹模板
     */
    @FieldComment("指纹模板")
    private String template;
    /**
     * 登记时间
     */
    @FieldComment("登记时间")
    @CompareIgnore
    private LocalDateTime enrollTime;
    /**
     * (最后)辨识时间
     */
    @FieldComment("(最后)辨识时间")
    @CompareIgnore
    private LocalDateTime identifyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
