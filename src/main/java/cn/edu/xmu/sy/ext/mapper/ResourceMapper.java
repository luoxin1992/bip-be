/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface ResourceMapper {
    Integer save(ResourceDO domain);

    Integer saveBatch(List<ResourceDO> domains);

    Integer updateById(ResourceDO domain);

    Integer removeById(Long id);

    ResourceDO getById(Long id);

    List<ResourceDO> getByType(String type);

    ResourceDO getByTag(@Param("type") String type, @Param("tag") String tag);

    Long countByParam(ResourceQueryParam param);

    List<ResourceDO> listByParam(ResourceQueryParam param);

    Long countAll();

    List<ResourceDO> listAll(@Param("offset") Long offset, @Param("rows") Integer rows);
}
