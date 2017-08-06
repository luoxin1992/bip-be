/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.List;

/**
 * 一般业务Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class GeneralBusinessMessage extends BaseSendMessage {
    /**
     * 附加信息
     */
    private List<String> extras;

    public GeneralBusinessMessage() {
        super(MessageTypeEnum.GENERAL_BUSINESS);
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }
}
