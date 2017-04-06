/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * (客户端)会话离线Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionOfflineParam extends BaseParam {
    /**
     * 消息队列名称
     */
    @NotNull
    @Max(32)
    private String queue;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
