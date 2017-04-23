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
     * 时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime timestamp;
    /**
     * 类别
     */
    private String category;
    /**
     * 内容
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
