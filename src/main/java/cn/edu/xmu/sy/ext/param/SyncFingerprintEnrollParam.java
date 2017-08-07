/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 同步新增指纹Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class SyncFingerprintEnrollParam extends BaseParam {
    /**
     * 用户编号
     */
    @NotNull
    @Size(min = 1, max = 16)
    private String number;
    /**
     * 指纹模板
     */
    @NotNull
    @Size(min = 1, max = 3072)
    private String template;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
