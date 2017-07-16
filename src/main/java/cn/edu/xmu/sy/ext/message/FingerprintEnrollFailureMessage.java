/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹登记失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollFailureMessage extends BaseSendMessage {
    public FingerprintEnrollFailureMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL_FAILURE);
    }
}
