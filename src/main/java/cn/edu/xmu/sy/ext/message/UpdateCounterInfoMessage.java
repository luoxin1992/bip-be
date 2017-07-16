/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 更新窗口信息Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class UpdateCounterInfoMessage extends BaseSendMessage {
    /**
     * 窗口编号
     */
    private String number;
    /**
     * 窗口名称
     */
    private String name;

    public UpdateCounterInfoMessage() {
        super(MessageTypeEnum.UPDATE_COUNTER_INFO);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
