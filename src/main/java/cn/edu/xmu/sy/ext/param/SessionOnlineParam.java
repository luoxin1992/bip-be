/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 会话上线Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionOnlineParam extends BaseParam {
    /**
     * 窗口ID
     */
    @NotNull
    @Min(1)
    private Long counterId;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }
}
