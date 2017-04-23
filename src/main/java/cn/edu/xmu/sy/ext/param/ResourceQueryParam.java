/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BasePeriodParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 资源查询Param
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class ResourceQueryParam extends BaseParam {
    /**
     * 时间区间
     */
    @NotNull
    @Valid
    private BasePeriodParam period;
    /**
     * 分页参数
     */
    @NotNull
    @Valid
    private BasePagingParam paging;

    public BasePeriodParam getPeriod() {
        return period;
    }

    public void setPeriod(BasePeriodParam period) {
        this.period = period;
    }

    public BasePagingParam getPaging() {
        return paging;
    }

    public void setPaging(BasePagingParam paging) {
        this.paging = paging;
    }
}
