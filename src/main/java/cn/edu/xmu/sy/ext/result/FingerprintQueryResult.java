/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 指纹查询Result
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class FingerprintQueryResult extends BaseResult {
    private Long id;
    private Long userId;
    private String finger;
    private String enrollTime;
    private String identifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public String getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(String enrollTime) {
        this.enrollTime = enrollTime;
    }

    public String getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(String identifyTime) {
        this.identifyTime = identifyTime;
    }
}
