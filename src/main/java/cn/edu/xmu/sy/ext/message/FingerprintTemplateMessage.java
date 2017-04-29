/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹模型Message(返回)
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintTemplateMessage extends BaseMessage {
    /**
     * 对应父消息ID
     */
    private Long parent;
    /**
     * 指纹模型
     */
    private String template;

    public FingerprintTemplateMessage() {
        super(MessageTypeEnum.FINGERPRINT_TEMPLATE);
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
