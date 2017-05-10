/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 查询全部用户(简版)Result
 *
 * @author luoxin
 * @version 2017-5-8
 */
public class UserListSimpleResult extends BaseResult {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户编号
     */
    private String number;
    /**
     * 指纹模板
     */
    private List<FingerprintTemplateQueryResult> fingerprints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<FingerprintTemplateQueryResult> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(List<FingerprintTemplateQueryResult> fingerprints) {
        this.fingerprints = fingerprints;
    }
}
