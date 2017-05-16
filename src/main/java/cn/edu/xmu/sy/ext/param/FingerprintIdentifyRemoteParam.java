/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 辨识远程指纹Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintIdentifyRemoteParam extends BaseParam {
    /**
     * 指纹模板
     */
    private String template;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
