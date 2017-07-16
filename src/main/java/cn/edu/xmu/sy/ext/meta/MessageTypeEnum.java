/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 消息类型枚举
 *
 * @author luoxin
 * @version 2017-4-27
 */
public enum MessageTypeEnum {
    UNKNOWN(null, "未知"),
    //发送的消息
    BUSINESS_PAUSE("business-pause", "业务暂停受理"),
    BUSINESS_RESUME("business-resume", "业务恢复受理"),
    BUSINESS_PROCESS("business-process", "业务正在受理"),
    BUSINESS_SUCCESS("business-success", "业务受理成功"),
    BUSINESS_FAILURE("business-failure", "业务受理失败"),
    FINGERPRINT_ENROLL("fingerprint-enroll", "指纹登记"),
    FINGERPRINT_ENROLL_SUCCESS("fingerprint-enroll-success", "指纹登记成功"),
    FINGERPRINT_ENROLL_FAILURE("fingerprint-enroll-failure", "指纹登记失败"),
    FINGERPRINT_IDENTIFY("fingerprint-identify", "指纹辨识"),
    FINGERPRINT_IDENTIFY_SUCCESS("fingerprint-identify-success", "指纹辨识成功"),
    FINGERPRINT_IDENTIFY_FAILURE("fingerprint-identify-failure", "指纹辨识失败"),
    BACK_HOME("back-home", "返回主界面"),
    UPDATE_COMPANY_INFO("update-company-info", "更新公司信息"),
    UPDATE_COUNTER_INFO("update-counter-info", "更新窗口信息"),
    UPDATE_USER_INFO("update-user-info", "更新用户信息"),
    //接收的消息
    ACK("ack", "确认"),
    FINGERPRINT_ENROLL_REPLY("fingerprint-enroll-reply", "指纹登记回复"),
    FINGERPRINT_IDENTIFY_REPLY("fingerprint-identify-reply", "指纹辨识回复"),;

    private String type;
    private String description;

    MessageTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByType(String type) {
        Optional<String> result = Arrays.stream(values())
                .filter(value -> Objects.equals(value.type, type))
                .map(MessageTypeEnum::getDescription)
                .findFirst();
        return result.orElse(UNKNOWN.getDescription());
    }
}
