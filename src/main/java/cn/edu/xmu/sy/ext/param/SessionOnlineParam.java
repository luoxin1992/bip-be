/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.constant.RegularExpression;

/**
 * (客户端)会话上线Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionOnlineParam {
    @ValidateRule(comment = "MAC地址", regExp = RegularExpression.MAC_ADDRESS, maxLen = 16)
    private String mac;
    @ValidateRule(comment = "IP地址", regExp = RegularExpression.IP_ADDRESS, maxLen = 16)
    private String ip;
    @ValidateRule(comment = "队列名称", maxLen = 32)
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
