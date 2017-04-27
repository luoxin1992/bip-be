/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * WebSocket消息类型枚举
 *
 * @author luoxin
 * @version 2017-4-27
 */
public enum MessageTypeEnum {
    /**
     * ACK
     */
    ACK("ack"),
    /**
     * 心跳
     */
    HEARTBEAT("heartbeat"),
    /**
     * 用户(客户)登录
     */
    USER_LOGIN("user-login"),
    /**
     * 柜台暂停服务
     */
    COUNTER_SERVICE_PAUSE("counter-service-pause"),
    /**
     * 柜台恢复服务
     */
    COUNTER_SERVICE_RESUME("counter-service-resume"),
    /**
     * 指纹登记
     */
    FINGERPRINT_ENROLL("fingerprint-enroll"),
    /**
     * 指纹辨识
     */
    FINGERPRINT_IDENTIFY("fingerprint-identify"),
    /**
     * 指纹模型(上行)
     */
    FINGERPRINT_TEMPLATE("fingerprint-template"),
    /**
     * 第三方业务开始受理
     */
    THIRD_PARTY_BIZ_START("third-party-biz-start"),
    /**
     * 第三方业务受理成功
     */
    THIRD_PARTY_BIZ_SUCCESSFUL("third-party-biz-successful"),
    /**
     * 第三方业务受理失败
     */
    THIRD_PARTY_BIZ_FAILED("third-party-biz-failed");

    private String type;

    MessageTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
