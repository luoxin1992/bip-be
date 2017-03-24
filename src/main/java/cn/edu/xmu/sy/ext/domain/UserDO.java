/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

/**
 * 用户Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class UserDO extends BaseDO {
    /**
     * 编号
     */
    @FieldComment("编号")
    private String number;
    /**
     * 姓名
     */
    @FieldComment("姓名")
    private String name;
    /**
     * 照片
     */
    @FieldComment("照片")
    private String photo;

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
}
