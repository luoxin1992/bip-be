/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹辨识成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifySuccessMessage extends BaseSendMessage {
    /**
     * 附加信息
     */
    private String extra;

    public FingerprintIdentifySuccessMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_SUCCESS);
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
