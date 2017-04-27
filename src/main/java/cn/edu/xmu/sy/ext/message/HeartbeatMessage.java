/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 心跳Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class HeartbeatMessage extends BaseMessage {
    public HeartbeatMessage() {
        super(MessageTypeEnum.HEARTBEAT.getType());
    }
}
