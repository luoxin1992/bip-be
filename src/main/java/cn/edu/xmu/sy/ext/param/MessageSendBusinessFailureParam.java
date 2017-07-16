/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Size;

/**
 * 业务受理失败 消息发送Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendBusinessFailureParam extends BaseParam {
    /**
     * 附加信息
     */
    @Size(min = 1, max = 128)
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
