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
public enum WorkerEnum {
    DEFAULT(0, "default");

    private Integer code;
    private String name;

    WorkerEnum(Integer code, String name) {
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
