/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 业务暂停受理Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class ServicePauseMessage extends BaseSendMessage {
    public ServicePauseMessage() {
        super(MessageTypeEnum.SERVICE_PAUSE);
    }
}
