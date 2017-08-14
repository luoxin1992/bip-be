/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 业务正在受理 消息发送Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendGeneralBusinessParam extends MessageSendBaseParam {
    /**
     * 超时时间
     */
    @NotNull
    @Max(300)
    private Integer timeout;
    /**
     * 附加信息
     */
    @Size(min = 1, max = 128)
    private String extra;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
