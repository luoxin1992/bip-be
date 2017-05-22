/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 日志类型查询Result
 *
 * @author luoxin
 * @version 2017-4-23
 */
public class LogTypeListResult extends BaseResult {
    /**
     * 类型
     */
    private List<String> types;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
