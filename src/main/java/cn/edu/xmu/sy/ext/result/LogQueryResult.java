/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 日志查询Result
 *
 * @author luoxin
 * @version 2017-4-23
 */
public class LogQueryResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * 内容
     */
    private String content;
    /**
     * 时间戳
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime timestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
