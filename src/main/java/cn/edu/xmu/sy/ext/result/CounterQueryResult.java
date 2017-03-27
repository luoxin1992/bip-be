/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 柜台查询Result
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class CounterQueryResult extends BaseResult {
    private Long id;
    private String number;
    private String name;
    private String mac;
    private String ip;
    private List<SessionQueryResult> sessions;

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

    public List<SessionQueryResult> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionQueryResult> sessions) {
        this.sessions = sessions;
    }
}
