/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 删除指纹Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintDeleteParam extends BaseParam {
    /**
     * 指纹ID
     */
    @NotNull
    @Min(1)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
