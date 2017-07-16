/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 接收的消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseReceiveMessage {
    /**
     * (回复的)消息ID
     */
    private Long uid;
    /**
     * 类型
     */
    private String type;

    BaseReceiveMessage(MessageTypeEnum type) {
        this.type = type.getType();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
