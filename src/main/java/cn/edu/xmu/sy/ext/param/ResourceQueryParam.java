/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-3-27
 */
public class ResourceQueryParam extends BaseParam {
    /**
     * 类型
     */
    @ValidateRule(comment = "类型", nullable = true)
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
