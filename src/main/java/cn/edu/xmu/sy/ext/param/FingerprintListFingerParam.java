/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 查询可用手指名称Param
 *
 * @author luoxin
 * @version 2017-5-16
 */
public class FingerprintListFingerParam extends BaseParam {
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
