/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 指纹辨识消息Result
 *
 * @author luoxin
 * @version 2017-5-14
 */
public class MessageFingerprintIdentifyResult extends BaseResult {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户编号
     */
    private String number;

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
}
