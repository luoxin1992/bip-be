/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 在线会话查询Result
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionQuerySimpleResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 窗口ID
     */
    private Long counterId;
    /**
     * Token
     */
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
