/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹辨识失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyFailureMessage extends BaseSendMessage {
    public FingerprintIdentifyFailureMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_FAILURE);
    }
}
