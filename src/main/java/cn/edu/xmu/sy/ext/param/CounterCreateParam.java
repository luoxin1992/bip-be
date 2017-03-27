/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.constant.RegularExpression;

/**
 * 创建柜台Param
 *
 * @author luoxin
 * @version 2017-3-23
 */
public class CounterCreateParam {
    @ValidateRule(comment = "编号", regExp = RegularExpression.NUMBER, maxLen = 16)
    private String number;
    @ValidateRule(comment = "名称", maxLen = 32)
    private String name;
    @ValidateRule(comment = "MAC地址", regExp = RegularExpression.MAC_ADDRESS, maxLen = 16)
    private String mac;
    @ValidateRule(comment = "IP地址", regExp = RegularExpression.IP_ADDRESS, maxLen = 16)
    private String ip;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
