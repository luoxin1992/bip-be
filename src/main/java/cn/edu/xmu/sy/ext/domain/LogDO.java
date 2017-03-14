/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.domain.BaseDO;

/**
 * 日志Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class LogDO extends BaseDO {
    /**
     * 客户端平台
     */
    private String platform;
    /**
     * 客户端版本
     */
    private String version;
    /**
     * 一级类别
     */
    private String primaryCategory;
    /**
     * 二级类别
     */
    private String secondaryCategory;
    /**
     * 内容
     */
    private String content;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
