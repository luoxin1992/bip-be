/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

/**
 * 指纹登记回复Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollReplyMessage extends BaseReceiveMessage {
    /**
     * 状态
     */
    private String status;
    /**
     * 指纹模型
     */
    private String template;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
