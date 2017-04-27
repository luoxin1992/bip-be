/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 柜台暂停服务Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class CounterServicePauseMessage extends BaseMessage {
    public CounterServicePauseMessage() {
        super(MessageTypeEnum.COUNTER_SERVICE_PAUSE.getType());
    }
}
