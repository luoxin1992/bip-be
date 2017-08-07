/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BaseSearchParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 查询用户Param
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class UserQueryParam extends BaseParam {
    /**
     * 搜索参数
     */
    @NotNull
    @Valid
    private BaseSearchParam search;
    /**
     * 分页参数
     */
    @NotNull
    @Valid
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
