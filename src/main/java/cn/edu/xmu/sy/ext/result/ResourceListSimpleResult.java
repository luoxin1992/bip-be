/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询全部资源Result(简版结果)
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class ResourceListSimpleResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 类型
     */
    private String type;
    /**
     * URI
     */
    private String uri;
    /**
     * MD5
     */
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
