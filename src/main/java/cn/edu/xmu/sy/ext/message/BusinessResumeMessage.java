/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 业务恢复受理Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessResumeMessage extends BaseSendMessage {
    public BusinessResumeMessage() {
        super(MessageTypeEnum.BUSINESS_RESUME);
    }
}
