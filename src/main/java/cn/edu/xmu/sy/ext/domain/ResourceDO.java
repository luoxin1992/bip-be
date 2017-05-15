/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.CompareIgnore;
import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

/**
 * 资源Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class ResourceDO extends BaseDO {
    /**
     * 类型
     */
    @FieldComment("类型")
    private String type;
    /**
     * 名称
     */
    @FieldComment("名称")
    private String name;
    /**
     * 文件名
     */
    @FieldComment("文件名")
    private String filename;
    /**
     * MD5
     */
    @FieldComment("MD5")
    @CompareIgnore
    private String md5;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
