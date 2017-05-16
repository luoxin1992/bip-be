/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 登记远程指纹Param
 *
 * @author luoxin
 * @version 2017-5-12
 */
public class FingerprintEnrollRemoteParam extends BaseParam {
    /**
     * UUID
     */
    private String uuid;
    /**
     * 指纹模板
     */
    private String template;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
