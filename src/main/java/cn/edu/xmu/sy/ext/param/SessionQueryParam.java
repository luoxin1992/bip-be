/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 查询会话Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionQueryParam extends BaseParam {
    /**
     * 窗口ID
     */
    @NotNull
    @Min(1)
    private Long counterId;
    @NotNull
    @Min(1)
    @Max(100)
    private Integer limit;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
