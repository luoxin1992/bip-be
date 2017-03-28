/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 资源查询结果
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class ResourceQueryResult extends BaseResult {
    /**
     * 资源
     */
    private List<ResourceItemQueryResult> resources;

    public List<ResourceItemQueryResult> getResources() {
        return resources;
    }

    public void setResources(List<ResourceItemQueryResult> resources) {
        this.resources = resources;
    }
}
