/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询资源简版Result
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class ResourceQuerySimpleResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 标签
     */
    private String tag;
    /**
     * URL
     */
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
