/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 消息状态枚举
 *
 * @author luoxin
 * @version 2017-4-27
 */
public enum MessageStatusEnum {
    N_ACK(0, "未确认"),
    ACK(1, "已确认"),
    ACK_OK(2, "已确认(成功)");

    private int status;
    private String description;

    MessageStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
