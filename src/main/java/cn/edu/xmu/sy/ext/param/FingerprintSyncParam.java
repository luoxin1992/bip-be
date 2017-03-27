/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 指纹(Pull)同步Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class FingerprintSyncParam extends BaseParam {

    private String userNumber;
    @ValidateRule(comment = "手指名称", maxLen = 16)
    private String finger;
    @ValidateRule(comment = "指纹模板", maxLen = 3072)
    private String template;

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
