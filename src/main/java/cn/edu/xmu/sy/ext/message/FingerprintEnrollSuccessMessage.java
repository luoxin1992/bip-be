/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹登记成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollSuccessMessage extends BaseMessage {
    /**
     * 图像/声音资源
     */
    private BaseMessageResource resource;

    public FingerprintEnrollSuccessMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL_SUCCESS);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
