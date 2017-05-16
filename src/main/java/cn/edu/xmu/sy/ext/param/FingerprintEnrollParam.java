/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 指纹登记Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintEnrollParam extends BaseParam {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 手指名称
     */
    private String finger;
    /**
     * 指纹模板
     */
    private String template;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
