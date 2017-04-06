/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.constant.RegExpConstant;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * (客户端)会话上线Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionOnlineParam extends BaseParam {
    /**
     * MAC地址
     */
    @NotNull
    @Max(16)
    @Pattern(regexp = RegExpConstant.MAC_ADDRESS)
    private String mac;
    /**
     * IP地址
     */
    @NotNull
    @Max(16)
    @Pattern(regexp = RegExpConstant.IP_ADDRESS)
    private String ip;
    /**
     * 消息队列名称
     */
    @NotNull
    @Max(32)
    private String queue;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
