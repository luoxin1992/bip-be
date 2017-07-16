/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 指纹登记 消息发送Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendFingerprintEnrollParam extends MessageSendBaseParam {
    /**
     * 用户ID
     */
    @Min(1)
    private Long userId;
    /**
     * 手指
     */
    @NotNull
    @Size(max = 9)
    private Integer finger;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getFinger() {
        return finger;
    }

    public void setFinger(Integer finger) {
        this.finger = finger;
    }
}
