/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 返回主界面Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class BackHomeMessage extends BaseSendMessage {
    public BackHomeMessage() {
        super(MessageTypeEnum.BACK_HOME);
    }
}
