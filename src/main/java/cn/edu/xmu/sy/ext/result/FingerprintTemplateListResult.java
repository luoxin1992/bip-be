/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询全部指纹模型Result
 *
 * @author luoxin
 * @version 2017-5-9
 */
public class FingerprintTemplateListResult extends BaseResult {
    /**
     * UUID
     */
    private String uuid;
    /**
     * 指纹模型
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
