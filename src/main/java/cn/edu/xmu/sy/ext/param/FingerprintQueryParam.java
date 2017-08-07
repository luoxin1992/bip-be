/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 查询指纹Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintQueryParam extends BaseParam {
    /**
     * 用户ID
     */
    @NotNull
    @Min(1)
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
