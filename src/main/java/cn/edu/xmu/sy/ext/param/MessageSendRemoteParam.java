/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 发送远程消息Param
 *
 * @author luoxin
 * @version 2017-5-2
 */
public class MessageSendRemoteParam extends BaseParam {
    /**
     * Token
     */
    @NotNull
    @Size(min = 32, max = 32)
    private String token;
    /**
     * 消息ID
     */
    @NotNull
    @Min(1)
    private Long id;
    /**
     * 消息体
     */
    @NotNull
    @Size(min = 1, max = 3072)
    private String body;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
