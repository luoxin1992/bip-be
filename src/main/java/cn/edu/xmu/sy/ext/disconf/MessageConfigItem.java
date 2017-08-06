/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.disconf;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * 消息配置项
 *
 * @author luoxin
 * @version 2017-5-22
 */
@Component
public class MessageConfigItem {
    /**
     * 心跳时间间隔
     */
    private int heartbeatPeriod;
    /**
     * 重试时间间隔
     */
    private int retryPeriod;
    /**
     * 重试次数上限
     */
    private int retryTimes;

    @DisconfItem(key = "message.heartbeat.period")
    public int getHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public void setHeartbeatPeriod(int heartbeatPeriod) {
        this.heartbeatPeriod = heartbeatPeriod;
    }

    @DisconfItem(key = "message.retry.period")
    public int getRetryPeriod() {
        return retryPeriod;
    }

    public void setRetryPeriod(int retryPeriod) {
        this.retryPeriod = retryPeriod;
    }

    @DisconfItem(key = "message.retry.times")
    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
