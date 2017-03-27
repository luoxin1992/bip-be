/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 编辑用户Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class UserModifyParam extends BaseParam {
    @ValidateRule(comment = "ID", minVal = 0)
    private Long id;
    @ValidateRule(comment = "编号", nullable = true, maxLen = 16)
    private String number;
    @ValidateRule(comment = "姓名", nullable = true, maxLen = 32)
    private String name;
    @ValidateRule(comment = "照片", nullable = true, maxLen = 128)
    private String photo;

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
}
