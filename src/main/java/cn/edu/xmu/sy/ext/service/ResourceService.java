/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.result.ResourceQuerySimpleResult;

import java.util.List;
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
    BaseListResult<ResourceListResult> list();

    /**
     * 查询资源
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<ResourceQueryResult> query(ResourceQueryParam param);

    /**
     * 根据类型查询资源
     *
     * @param type 类型
     * @return 查询结果
     */
    List<ResourceQuerySimpleResult> queryByType(String type);

    /**
     * 根据标签查询资源
     *
     * @param type 类型
     * @param tag  标签
     * @return 查询结果
     */
    Optional<ResourceQuerySimpleResult> queryByTag(String type, String tag);

    /**
     * 创建资源
     *
     * @param type     类型
     * @param tag      标签
     * @param filename 文件名
     */
    void create(String type, String tag, String filename);

    /**
     * 修改资源
     *
     * @param id       ID
     * @param filename 文件名
     */
    void modify(Long id, String filename);

    /**
     * 删除资源
     *
     * @param id ID
     */
    void delete(Long id);
}
