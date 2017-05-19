/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 指纹登记 消息Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageFingerprintEnrollParam extends BaseParam {
    /**
     * 消息接收目标
     */
    @NotNull
    @Valid
    private MessageSendToParam sendTo;
    /**
     * 用户ID(指纹登记目标)
     */
    @Min(1)
    private Long id;
    /**
     * 用户编号(指纹登记目标)
     */
    @Size(min = 1, max = 16)
    private String number;
    /**
     * 手指名称
     */
    @NotNull
    @Size(min = 1, max = 16)
    private String finger;

    public MessageSendToParam getSendTo() {
        return sendTo;
    }

    public void setSendTo(MessageSendToParam sendTo) {
        this.sendTo = sendTo;
    }

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

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }
}
