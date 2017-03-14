/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 实体枚举
 * (用一个整数表示一个实体)
 *
 * @author luoxin
 * @version 2017-3-14
 */
public enum EntityEnum {
    USER(1, "user"),
    FINGERPRINT(2, "fingerprint"),
    COUNTER(3, "counter"),
    SESSION(4, "session"),
    MESSAGE(5, "message"),
    SETTING(6, "setting"),
    RESOURCE(7, "resource"),
    LOG(8, "log");

    private Integer code;
    private String name;

    EntityEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
