/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseParam;
import cn.com.lx1992.lib.base.param.BaseSearchParam;

/**
 * 查询用户Param
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class UserQueryParam extends BaseParam {
    @ValidateRule(comment = "搜索参数")
    private BaseSearchParam search;
    @ValidateRule(comment = "分页参数")
    private BasePagingParam paging;

    public UserQueryParam() {
        search = new BaseSearchParam();
        paging = new BasePagingParam();
    }

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
