/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹辨识Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyMessage extends BaseSendMessage {
    /**
     * 超时时间
     */
    private Long timeout;

    public FingerprintIdentifyMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY);
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
