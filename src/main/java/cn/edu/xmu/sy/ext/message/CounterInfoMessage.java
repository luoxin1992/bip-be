/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 更新柜台信息Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class CounterInfoMessage extends BaseMessage {
    /**
     * 柜台编号
     */
    private String number;
    /**
     * 柜台名称
     */
    private String name;

    public CounterInfoMessage() {
        super(MessageTypeEnum.COUNTER_INFO);
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
