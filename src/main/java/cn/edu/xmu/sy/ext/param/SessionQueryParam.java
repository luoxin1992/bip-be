/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.param.BasePeriodParam;

/**
 * 查询会话Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionQueryParam {
    @FieldComment("柜台ID")
    private Long counterId;
    @FieldComment("会话状态")
    private Integer status;
    @FieldComment("时间区间")
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
