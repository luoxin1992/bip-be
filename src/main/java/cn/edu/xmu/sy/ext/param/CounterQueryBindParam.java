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
 * 根据MAC地址和IP地址查询窗口Param
 *
 * @author luoxin
 * @version 2017-6-27
 */
public class CounterQueryBindParam extends BaseParam {
    /**
     * MAC地址
     */
    @NotNull
    @Size(min = 1, max = 32)
    @Pattern(regexp = RegExpConstant.MAC_ADDRESS)
    private String mac;
    /**
     * IP地址
     */
    @NotNull
    @Size(min = 1, max = 16)
    @Pattern(regexp = RegExpConstant.IP_ADDRESS)
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
