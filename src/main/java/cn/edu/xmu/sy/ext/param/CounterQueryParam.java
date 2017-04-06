/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BaseSearchParam;

import javax.validation.constraints.NotNull;

/**
 * 柜台查询Param
 *
 * @author luoxin
 * @version 2017-3-25
 */
public class CounterQueryParam extends BaseParam {
    /**
     * 搜索参数
     */
    @NotNull
    private BaseSearchParam search;
    /**
     * 分页参数
     */
    @NotNull
    private BasePagingParam paging;

    public BaseSearchParam getSearch() {
        return search;
    }

    public void setSearch(BaseSearchParam search) {
        this.search = search;
    }

    public BasePagingParam getPaging() {
        return paging;
    }

    public void setPaging(BasePagingParam paging) {
        this.paging = paging;
    }
}
