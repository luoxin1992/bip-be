/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import java.util.Arrays;
import java.util.Optional;

/**
 * 会话状态枚举
 *
 * @author luoxin
 * @version 2017-3-24
 */
public enum SessionStatusEnum {
    ONLINE(1, "在线"),
    OFFLINE(2, "离线"),
    LOST(3, "失联"),
    UNKNOWN(999, "未知");

    private int status;
    private String description;

    SessionStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByStatus(int status) {
        Optional<String> result = Arrays.stream(values())
                .filter((value) -> value.status == status)
                .map(SessionStatusEnum::getDescription)
                .findFirst();
        return result.orElse(UNKNOWN.getDescription());
    }
}
