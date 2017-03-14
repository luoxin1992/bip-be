/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.ResourceDO;
import org.apache.ibatis.annotations.Mapper;

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
}
