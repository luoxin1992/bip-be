/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * (客户端)会话失联Param
 *
 * @author luoxin
 * @version 2017-4-27
 */
public class SessionLostParam extends BaseParam {
    /**
     * Token
     */
    @NotNull
    @Size(min = 1,max = 16)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
