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
public class BusinessFailureMessage extends BaseMessage {
    /**
     * 附加内容
     */
    private String extra;
    /**
     * 图像/声音资源
     */
    private BaseMessageResource resource;

    public BusinessFailureMessage() {
        super(MessageTypeEnum.BUSINESS_FAILURE);
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
