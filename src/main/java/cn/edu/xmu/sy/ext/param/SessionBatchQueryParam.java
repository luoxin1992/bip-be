/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.param.BasePeriodParam;

import java.util.List;

/**
 * 批量查询会话Param
 *
 * @author luoxin
 * @version 2017-3-24
 */
public class SessionBatchQueryParam {
    @FieldComment("柜台ID")
    private List<Long> counterIds;
    @FieldComment("会话状态")
    private Integer status;
    @FieldComment("时间区间")
    private BasePeriodParam period;

    public List<Long> getCounterIds() {
        return counterIds;
    }

    public void setCounterIds(List<Long> counterIds) {
        this.counterIds = counterIds;
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
