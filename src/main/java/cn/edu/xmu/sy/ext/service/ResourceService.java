/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.result.ResourceQueryResult;

/**
 * 资源Service
 *
 * @author luoxin
 * @version 2017-3-27
 */
public interface ResourceService {
    /**
     * 查询全部资源
     *
     * @return 查询结果
     */
    ResourceQueryResult query();

    /**
     * 新增资源
     *
     * @param type 类型
     * @param name 名称
     * @param path 路径
     */
    void create(String type, String name, String path);

    /**
     * 根据类型和名称查询资源ID
     *
     * @param type 类型
     * @param name 名称
     * @return 资源ID
     */
    Long getIdByTypeAndName(String type, String name);
}
