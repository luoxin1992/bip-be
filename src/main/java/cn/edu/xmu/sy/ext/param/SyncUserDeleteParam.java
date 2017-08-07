/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 同步删除用户Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class SyncUserDeleteParam extends BaseParam {
    /**
     * 用户编号
     */
    @NotNull
    @Size(min = 1, max = 16)
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}