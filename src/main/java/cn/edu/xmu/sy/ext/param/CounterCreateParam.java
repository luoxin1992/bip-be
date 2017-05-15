/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.constant.RegExpConstant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 创建窗口Param
 *
 * @author luoxin
 * @version 2017-3-23
 */
public class CounterCreateParam extends BaseParam {
    /**
     * 编号
     */
    @NotNull
    @Size(min = 1, max = 16)
    @Pattern(regexp = RegExpConstant.NUMBER)
    private String number;
    /**
     * 名称
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    /**
     * MAC地址
     */
    @NotNull
    @Size(min = 1, max = 16)
    @Pattern(regexp = RegExpConstant.MAC_ADDRESS)
    private String mac;
    /**
     * IP地址
     */
    @NotNull
    @Size(min = 1, max = 16)
    @Pattern(regexp = RegExpConstant.IP_ADDRESS)
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
