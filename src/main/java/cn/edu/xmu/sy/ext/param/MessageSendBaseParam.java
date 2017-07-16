/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 消息发送Param基类
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendBaseParam extends BaseParam {
    /**
     * 消息发送目标(接收窗口ID)
     */
    @NotNull
    @Min(1)
    private Long sendTo;

    public Long getSendTo() {
        return sendTo;
    }

    public void setSendTo(Long sendTo) {
        this.sendTo = sendTo;
    }
}
