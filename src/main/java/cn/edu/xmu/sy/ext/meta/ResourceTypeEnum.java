/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import java.util.Objects;

/**
 * 资源类型枚举
 *
 * @author luoxin
 * @version 2017-3-28
 */
public enum ResourceTypeEnum {
    VOICE("voice", "语音"),
    IMAGE("image", "图片"),
    UNKNOWN("unknown", "未知");

    private String type;
    private String description;

    ResourceTypeEnum(String type, String description) {
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
        for (ResourceTypeEnum value : values()) {
            if (Objects.equals(value.type, type)) {
                return value.getDescription();
            }
        }
        return UNKNOWN.getDescription();
    }
}
