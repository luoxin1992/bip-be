/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BasePeriodParam;
import cn.com.lx1992.lib.base.param.BaseSearchParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 日志查询Param
 *
 * @author luoxin
 * @version 2017-4-18
 */
public class LogQueryParam extends BaseParam {
    /**
     * 类别
     */
    private String category;
    /**
     * 搜索参数
     */
    @NotNull
    @Valid
    private BaseSearchParam search;
    /**
     * 时间参数
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BaseSearchParam getSearch() {
        return search;
    }

    public void setSearch(BaseSearchParam search) {
        this.search = search;
    }

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
