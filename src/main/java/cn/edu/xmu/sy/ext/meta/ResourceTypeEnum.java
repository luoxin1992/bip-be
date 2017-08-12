/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import java.util.Arrays;
import java.util.Objects;

/**
 * 资源类型枚举
 *
 * @author luoxin
 * @version 2017-3-28
 */
public enum ResourceTypeEnum {
    UNKNOWN(null, "未知"),
    VOICE("voice", "语音"),
    IMAGE("image", "图片");

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
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.type, type))
                .map(ResourceTypeEnum::getDescription)
                .findFirst()
                .orElse(UNKNOWN.description);
    }
}
