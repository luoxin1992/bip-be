/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 一般业务失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class GeneralBusinessFailureMessage extends BaseSendMessage {
    /**
     * 附加信息
     */
    private String extra;

    public GeneralBusinessFailureMessage() {
        super(MessageTypeEnum.GENERAL_BUSINESS_FAILURE);
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
