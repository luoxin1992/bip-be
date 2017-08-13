/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 手指名称枚举
 *
 * @author luoxin
 * @version 2017-5-11
 */
public enum FingerprintFingerEnum {
    UNKNOWN(-1, "未知"),
    LEFT_THUMB(0, "左手拇指"),
    LEFT_INDEX_FINGER(1, "左手食指"),
    LEFT_MIDDLE_FINGER(2, "左手中指"),
    LEFT_RING_FINGER(3, "左手无名指"),
    LEFT_LITTLE_FINGER(4, "左手小指"),
    RIGHT_THUMB(5, "右手拇指"),
    RIGHT_INDEX_FINGER(6, "右手食指"),
    RIGHT_MIDDLE_FINGER(7, "右手中指"),
    RIGHT_RING_FINGER(8, "右手无名指"),
    RIGHT_LITTLE_FINGER(9, "右手小指");

    private Integer finger;
    private String description;

    FingerprintFingerEnum(Integer code, String finger) {
        this.finger = code;
        this.description = finger;
    }

    public Integer getFinger() {
        return finger;
    }

    public String getDescription() {
        return description;
    }

    public static FingerprintFingerEnum getByFinger(Integer finger) {
        Optional<FingerprintFingerEnum> result = Arrays.stream(values())
                .filter(value -> Objects.equals(value.finger, finger))
                .findFirst();
        return result.orElse(UNKNOWN);
    }
}
