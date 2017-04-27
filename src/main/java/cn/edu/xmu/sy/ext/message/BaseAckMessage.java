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
     * 被确认消息ID
     */
    private Long id;
    /**
     * 成功标记
     */
    private Boolean success;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
