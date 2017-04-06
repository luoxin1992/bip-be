/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.CompareIgnore;
import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

import java.time.LocalDateTime;

/**
 * 会话Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class SessionDO extends BaseDO {
    /**
     * 柜台ID
     */
    @FieldComment("柜台ID")
    private Long counterId;
    /**
     * 消息队列名称
     */
    @FieldComment("消息队列名称")
    private String queue;
    /**
     * 状态
     */
    @FieldComment("状态")
    private Integer status;
    /**
     * 上线时间
     */
    @FieldComment("上线时间")
    @CompareIgnore
    private LocalDateTime onlineTime;
    /**
     * 下线时间
     */
    @FieldComment("下线时间")
    @CompareIgnore
    private LocalDateTime offlineTime;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(LocalDateTime onlineTime) {
        this.onlineTime = onlineTime;
    }

    public LocalDateTime getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(LocalDateTime offlineTime) {
        this.offlineTime = offlineTime;
    }
}
