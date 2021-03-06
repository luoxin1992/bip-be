/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 一般业务Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class GeneralBusinessMessage extends BaseSendMessage {
    /**
     * 超时时间
     */
    private Integer timeout;
    /**
     * 附加信息
     */
    private List<String> extras;

    public GeneralBusinessMessage() {
        super(MessageTypeEnum.GENERAL_BUSINESS);
        this.extras = new ArrayList<>();
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }
}
