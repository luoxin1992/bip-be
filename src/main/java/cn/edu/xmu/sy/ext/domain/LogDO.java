/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

import java.time.LocalDateTime;

/**
 * 日志Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class LogDO extends BaseDO {
    /**
     * 类型
     */
    @FieldComment("类型")
    private String type;
    /**
     * 内容
     */
    @FieldComment("内容")
    private String content;
    /**
     * 时间戳
     */
    @FieldComment("时间戳")
    private LocalDateTime timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
