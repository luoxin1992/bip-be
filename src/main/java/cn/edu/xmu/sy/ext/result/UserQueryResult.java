/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 查询用户Result
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class UserQueryResult extends BaseResult {
    /**
     * 用户ID
     */
    private Long id;
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片(URL)
     */
    private String photo;
    /**
     * 指纹
     */
    private List<FingerprintQueryResult> fingerprints;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<FingerprintQueryResult> getFingerprints() {
        return fingerprints;
    }

    public void setFingerprints(List<FingerprintQueryResult> fingerprints) {
        this.fingerprints = fingerprints;
    }
}
