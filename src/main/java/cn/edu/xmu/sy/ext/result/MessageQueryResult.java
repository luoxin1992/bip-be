/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

/**
 * 消息查询Result
 *
 * @author luoxin
 * @version 2017-5-14
 */
public class MessageQueryResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 窗口ID
     */
    @JsonIgnore
    private Long counterId;
    /**
     * 窗口信息
     */
    private CounterQueryResult counter;
    /**
     * 会话ID
     */
    @JsonIgnore
    private Long sessionId;
    /**
     * 会话信息
     */
    private SessionQueryResult session;
    /**
     * 消息ID
     */
    private Long uid;
    /**
     * 类型
     */
    private String type;
    /**
     * 消息体
     */
    private String body;
    /**
     * 重试次数
     */
    private Integer retry;
    /**
     * 发送时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime sendTime;
    /**
     * 确认时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime ackTime;
    /**
     * 消息回复
     */
    private MessageReplyQueryResult reply;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public CounterQueryResult getCounter() {
        return counter;
    }

    public void setCounter(CounterQueryResult counter) {
        this.counter = counter;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public SessionQueryResult getSession() {
        return session;
    }

    public void setSession(SessionQueryResult session) {
        this.session = session;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public MessageReplyQueryResult getReply() {
        return reply;
    }

    public void setReply(MessageReplyQueryResult reply) {
        this.reply = reply;
    }
}
