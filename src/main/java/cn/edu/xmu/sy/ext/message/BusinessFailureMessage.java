/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 业务受理失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessFailureMessage extends BaseSendMessage {
    public BusinessFailureMessage() {
        super(MessageTypeEnum.BUSINESS_FAILURE);
    }
}
