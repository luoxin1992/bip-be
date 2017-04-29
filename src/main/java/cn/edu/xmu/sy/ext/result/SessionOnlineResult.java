/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 会话上线Result
 *
 * @author luoxin
 * @version 2017-4-27
 */
public class SessionOnlineResult extends BaseResult {
    /**
     * Token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
