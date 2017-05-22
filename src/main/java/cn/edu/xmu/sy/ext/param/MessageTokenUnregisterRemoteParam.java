/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 解除注册远程Token Param
 *
 * @author luoxin
 * @version 2017-4-30
 */
public class MessageTokenUnregisterRemoteParam extends BaseParam {
    /**
     * Token
     */
    @NotNull
    @Size(min = 32, max = 32)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
