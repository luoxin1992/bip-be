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
     * 窗口ID
     */
    @FieldComment("窗口ID")
    private Long counterId;
    /**
     * 会话ID
     */
    @FieldComment("会话ID")
    private Long sessionId;
    /**
     * UID
     */
    @FieldComment("UID")
    private Long uid;
    /**
     * 方向
     */
    @FieldComment("方向")
    private Integer direction;
    /**
     * 类型
     */
    @FieldComment("类型")
    private String type;
    /**
     * 消息体
     */
    @FieldComment("消息体")
    private String body;
    /**
     * 重试次数
     */
    @FieldComment("重试次数")
    private Integer retry;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
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
