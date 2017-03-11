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
     * 业务类型
     */
    private String biz;
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

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
