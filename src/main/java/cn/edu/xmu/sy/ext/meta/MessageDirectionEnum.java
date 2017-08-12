/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 消息方向枚举
 *
 * @author luoxin
 * @version 2017-7-10
 */
public enum MessageDirectionEnum {
    SEND(0),
    RECEIVE(1);

    private Integer direction;

    MessageDirectionEnum(Integer direction) {
        this.direction = direction;
    }

    public Integer getDirection() {
        return direction;
    }
}
