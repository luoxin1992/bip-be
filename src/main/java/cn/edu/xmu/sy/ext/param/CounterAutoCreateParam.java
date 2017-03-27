/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.constant.RegularExpression;

/**
 * (自动)创建柜台param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class CounterAutoCreateParam {
    @ValidateRule(comment = "MAC地址", regExp = RegularExpression.MAC_ADDRESS, maxLen = 16)
    private String mac;
    @ValidateRule(comment = "IP地址", regExp = RegularExpression.IP_ADDRESS, maxLen = 16)
    private String ip;

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
}
