/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 业务恢复受理 消息Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageBusinessResumeParam extends BaseParam {
    /**
     * 消息接收目标
     */
    @NotNull
    @Valid
    private MessageSendToParam sendTo;

    public MessageSendToParam getSendTo() {
        return sendTo;
    }

    public void setSendTo(MessageSendToParam sendTo) {
        this.sendTo = sendTo;
    }
}
