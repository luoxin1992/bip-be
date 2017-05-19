/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新窗口信息 消息Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageCounterInfoParam extends BaseParam {
    /**
     * 消息接收目标
     */
    @NotNull
    @Valid
    private MessageSendToParam sendTo;
    /**
     * 窗口编号
     */
    @Size(min = 1, max = 16)
    @NotNull
    private String number;
    /**
     * 窗口名称
     */
    @Size(min = 1, max = 32)
    @NotNull
    private String name;

    public MessageSendToParam getSendTo() {
        return sendTo;
    }

    public void setSendTo(MessageSendToParam sendTo) {
        this.sendTo = sendTo;
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
}
