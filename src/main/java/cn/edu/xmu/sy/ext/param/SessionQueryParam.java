/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BasePeriodParam;

import javax.validation.constraints.NotNull;

/**
 * 查询会话Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionQueryParam extends BaseParam {
    /**
     * 柜台ID
     */
    @NotNull
    private Long counterId;
    /**
     * 会话状态
     */
    private Integer status;
    /**
     * 时间区间
     */
    @NotNull
    private BasePeriodParam period;

    public Long getCounterId() {
        return counterId;
    }

    public void setCounterId(Long counterId) {
        this.counterId = counterId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BasePeriodParam getPeriod() {
        return period;
    }

    public void setPeriod(BasePeriodParam period) {
        this.period = period;
    }
}
