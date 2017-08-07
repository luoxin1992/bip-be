/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询指纹模型Result
 *
 * @author luoxin
 * @version 2017-5-9
 */
public class FingerprintListTemplateResult extends BaseResult {
    /**
     * UID
     */
    private Integer uid;
    /**
     * 指纹模型
     */
    private String template;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
