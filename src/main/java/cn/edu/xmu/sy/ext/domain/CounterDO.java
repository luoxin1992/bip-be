/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

/**
 * 窗口Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class CounterDO extends BaseDO {
    /**
     * 编号
     */
    @FieldComment("编号")
    private String number;
    /**
     * 名称
     */
    @FieldComment("名称")
    private String name;
    /**
     * MAC地址
     */
    @FieldComment("MAC地址")
    private String mac;
    /**
     * IP地址
     */
    @FieldComment("IP地址")
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
