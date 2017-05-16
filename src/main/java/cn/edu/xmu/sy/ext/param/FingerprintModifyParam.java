/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改指纹Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintModifyParam extends BaseParam {
    /**
     * 指纹ID
     */
    @NotNull
    @Min(1)
    private Long id;
    /**
     * 手指名称
     */
    @NotNull
    @Size(min = 1, max = 16)
    private String finger;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }
}
