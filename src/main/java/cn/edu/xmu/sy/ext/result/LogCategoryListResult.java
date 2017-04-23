/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 日志类别查询Result
 *
 * @author luoxin
 * @version 2017-4-23
 */
public class LogCategoryListResult extends BaseResult {
    /**
     * 类别
     */
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
