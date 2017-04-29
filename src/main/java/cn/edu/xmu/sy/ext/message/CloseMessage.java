/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 关闭客户端Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class CloseMessage extends BaseMessage {
    public CloseMessage() {
        super(MessageTypeEnum.CLOSE);
    }
}
