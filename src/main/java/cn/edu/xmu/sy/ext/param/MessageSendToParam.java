/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 消息接收目标(发送至某个窗口)Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendToParam extends BaseParam {
    /**
     * 窗口ID
     */
    @Min(1)
    private Long id;
    /**
     * 窗口编号
     */
    @Size(min = 1, max = 16)
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
