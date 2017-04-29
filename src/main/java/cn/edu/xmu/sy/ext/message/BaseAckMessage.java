/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

/**
 * 确认Message基类
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BaseAckMessage {
    /**
     * 被确认消息UUID
     */
    private Long uuid;
    /**
     * 成功标记
     */
    private Boolean success;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
