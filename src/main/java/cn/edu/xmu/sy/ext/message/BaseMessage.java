/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * Message基类
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BaseMessage {
    /**
     * UUID
     */
    private String uuid;
    /**
     * 类型
     */
    private String type;

    public BaseMessage(MessageTypeEnum type) {
        this.uuid = UUIDUtil.randomUUID();
        this.type = type.getType();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
