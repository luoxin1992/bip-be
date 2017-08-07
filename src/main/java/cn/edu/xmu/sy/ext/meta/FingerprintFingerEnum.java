/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 手指名称枚举
 *
 * @author luoxin
 * @version 2017-5-11
 */
public enum FingerprintFingerEnum {
    UNKNOWN("未知"),
    LEFT_THUMB("左手拇指"),
    LEFT_INDEX_FINGER("左手食指"),
    LEFT_MIDDLE_FINGER("左手中指"),
    LEFT_RING_FINGER("左手无名指"),
    LEFT_LITTLE_FINGER("左手小指"),
    RIGHT_THUMB("右手拇指"),
    RIGHT_INDEX_FINGER("右手食指"),
    RIGHT_MIDDLE_FINGER("右手中指"),
    RIGHT_RING_FINGER("右手无名指"),
    RIGHT_LITTLE_FINGER("右手小指");

    private String finger;

    FingerprintFingerEnum(String finger) {
        this.finger = finger;
    }

    public String getFinger() {
        return finger;
    }
}
