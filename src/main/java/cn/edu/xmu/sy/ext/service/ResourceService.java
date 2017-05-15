/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.ResourceCreateParam;
import cn.edu.xmu.sy.ext.param.ResourceModifyParam;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListSimpleResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;

import java.util.Optional;

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
    BaseListResult<ResourceListSimpleResult> listSimple();

    /**
     * 查询资源
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<ResourceQueryResult> query(ResourceQueryParam param);

    /**
     * 新增资源
     *
     * @param param 创建参数
     */
    void create(ResourceCreateParam param);

    /**
     * 修改资源
     *
     * @param param 修改参数
     */
    void modify(ResourceModifyParam param);

    /**
     * 删除资源
     *
     * @param id 资源ID
     */
    void delete(Long id);

    /**
     * 根据类型和名称查询资源ID
     *
     * @param type 类型
     * @param name 名称
     * @return 资源ID(可选值)
     */
    Optional<Long> getIdByTypeAndName(String type, String name);
}
