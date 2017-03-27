/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 创建用户Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class UserCreateParam extends BaseParam {
    @ValidateRule(comment = "编号", maxLen = 16)
    private String number;
    @ValidateRule(comment = "姓名", maxLen = 32)
    private String name;
    @ValidateRule(comment = "照片", nullable = true, maxLen = 128)
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
