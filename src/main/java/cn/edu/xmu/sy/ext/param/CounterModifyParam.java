/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.constant.RegExpConstant;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 修改柜台Param
 *
 * @author luoxin
 * @version 2017-3-23
 */
public class CounterModifyParam extends BaseParam {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    @Max(16)
    @Pattern(regexp = RegExpConstant.NUMBER)
    private String number;
    @NotNull
    @Max(32)
    private String name;
    @NotNull
    @Max(16)
    @Pattern(regexp = RegExpConstant.MAC_ADDRESS)
    private String mac;
    @NotNull
    @Max(16)
    @Pattern(regexp = RegExpConstant.IP_ADDRESS)
    private String ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
