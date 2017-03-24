/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.CompareIgnore;
import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

import java.time.LocalDateTime;

/**
 * 消息Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class MessageDO extends BaseDO {
    /**
     * 柜台ID
     */
    @FieldComment("柜台ID")
    private Long counterId;
    /**
     * 会话ID
     */
    @FieldComment("会话ID")
    private Long sessionId;
    /**
     * 类型
     */
    @FieldComment("类型")
    private String type;
    /**
     * 附加信息
     */
    @FieldComment("附加信息")
    private String extra;
    /**
     * 发送时间
     */
    @FieldComment("发送时间")
    @CompareIgnore
    private LocalDateTime sendTime;
    /**
     * 确认时间
     */
    @FieldComment("确认时间")
    @CompareIgnore
    private LocalDateTime ackTime;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public LocalDateTime getAckTime() {
        return ackTime;
    }

    public void setAckTime(LocalDateTime ackTime) {
        this.ackTime = ackTime;
    }
}
