/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 编辑用户Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class UserModifyParam extends BaseParam {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    @Size(min = 1, max = 16)
    private String number;
    @NotNull
    @Size(min = 1, max = 32)
    private String name;
    @Size(min = 1, max = 128)
    private String photo;
    @Null
    private Boolean fromSync;

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

    public Boolean getFromSync() {
        return fromSync;
    }

    public void setFromSync(Boolean fromSync) {
        this.fromSync = fromSync;
    }
}
