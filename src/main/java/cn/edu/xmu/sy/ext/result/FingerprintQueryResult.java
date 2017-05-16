/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 指纹查询Result
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class FingerprintQueryResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 手指名称
     */
    private String finger;
    /**
     * 登记时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime enrollTime;
    /**
     * (最后)辨识时间
     */
    @JsonFormat(pattern = DateTimeConstant.DATETIME_PATTERN_WITH_BAR)
    private LocalDateTime identifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public LocalDateTime getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(LocalDateTime enrollTime) {
        this.enrollTime = enrollTime;
    }

    public LocalDateTime getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(LocalDateTime identifyTime) {
        this.identifyTime = identifyTime;
    }
}
