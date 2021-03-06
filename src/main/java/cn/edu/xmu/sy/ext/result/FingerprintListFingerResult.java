/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询手指Result
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintListFingerResult extends BaseResult {
    /**
     * 手指
     */
    private Integer finger;
    /**
     * 描述
     */
    private String description;

    public Integer getFinger() {
        return finger;
    }

    public void setFinger(Integer finger) {
        this.finger = finger;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
