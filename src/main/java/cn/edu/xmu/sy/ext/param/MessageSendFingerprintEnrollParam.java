/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 指纹登记 消息发送Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendFingerprintEnrollParam extends MessageSendBaseParam {
    /**
     * 用户(ID)
     */
    @Min(1)
    private Long user;
    /**
     * 手指
     */
    @NotNull
    @Min(0)
    @Max(9)
    private Integer finger;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Integer getFinger() {
        return finger;
    }

    public void setFinger(Integer finger) {
        this.finger = finger;
    }
}
