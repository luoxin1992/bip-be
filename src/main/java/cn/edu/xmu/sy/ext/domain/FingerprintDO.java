/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.domain.BaseDO;

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
    private Long userId;
    /**
     * 手指名称
     */
    private String finger;
    /**
     * 指纹模板
     */
    private String template;
    /**
     * 登记时间
     */
    private LocalDateTime enrollTime;
    /**
     * (最后)辨识时间
     */
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
