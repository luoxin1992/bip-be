/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 会话查询Result
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionQueryResult extends BaseResult {
    private Long id;
    private Long counterId;
    private String status;
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime onlineTime;
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime offlineTime;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
