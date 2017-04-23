/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 资源修改Param
 *
 * @author luoxin
 * @version 2017-4-7
 */
public class ResourceModifyParam extends BaseParam {
    /**
     * ID
     */
    @NotNull
    @Min(1)
    private Long id;
    /**
     * 类型
     */
    @NotNull
    @Pattern(regexp = "image|voice")
    private String type;
    /**
     * 名称
     */
    @NotNull
    @Size(min = 1, max = 64)
    private String name;
    /**
     * 路径
     */
    @NotNull
    @Size(min = 1, max = 128)
    private String path;
    /**
     * MD5
     */
    @NotNull
    @Size(min = 32, max = 32)
    private String md5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
