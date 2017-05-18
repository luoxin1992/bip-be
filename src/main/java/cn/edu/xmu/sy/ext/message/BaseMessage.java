/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseMessage {
    /**
     * (回复)消息ID
     */
    private Long id;
    /**
     * 类型
     */
    private String type;

    public BaseMessage() {
    }

    public BaseMessage(MessageTypeEnum typeEnum) {
        this.type = typeEnum.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
