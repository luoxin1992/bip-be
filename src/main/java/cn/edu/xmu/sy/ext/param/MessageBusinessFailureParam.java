/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 业务受理失败 消息Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageBusinessFailureParam extends BaseParam {
    /**
     * 消息接收目标
     */
    @NotNull
    @Valid
    private MessageSendToParam sendTo;
    /**
     * 附加信息
     */
    @Size(min = 1, max = 128)
    private String extra;

    public MessageSendToParam getSendTo() {
        return sendTo;
    }

    public void setSendTo(MessageSendToParam sendTo) {
        this.sendTo = sendTo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
