/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 删除用户Param
 *
 * @author luoxin
 * @version 2017-3-17
 */
public class UserDeleteParam extends BaseParam {
    @NotNull
    @Min(1)
    private Long id;
    @Null
    private Boolean fromSync;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFromSync() {
        return fromSync;
    }

    public void setFromSync(Boolean fromSync) {
        this.fromSync = fromSync;
    }
}