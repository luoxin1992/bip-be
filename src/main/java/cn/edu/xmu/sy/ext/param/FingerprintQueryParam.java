/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;

/**
 * 指纹查询参数
 *
 * @author luoxin
 * @version 2017-3-25
 */
public class FingerprintQueryParam {
    /**
     * 用户ID
     */
    @ValidateRule(comment = "用户ID", minVal = 1)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
