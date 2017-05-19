/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 消息回复(Msg服务回调)Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageReplyParam extends BaseParam {
    /**
     * 消息体
     */
    @NotNull
    @Size(min = 1, max = 3072)
    private String body;
    /**
     * 时间戳
     */
    @NotNull
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN)
    private LocalDateTime timestamp;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
