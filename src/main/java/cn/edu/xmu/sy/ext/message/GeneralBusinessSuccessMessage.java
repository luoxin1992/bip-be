/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 一般业务成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class GeneralBusinessSuccessMessage extends BaseSendMessage {
    /**
     * 附加信息
     */
    private String extra;

    public GeneralBusinessSuccessMessage() {
        super(MessageTypeEnum.GENERAL_BUSINESS_SUCCESS);
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
