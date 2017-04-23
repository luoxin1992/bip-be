/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.LogDO;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 日志Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface LogMapper {
    Integer save(LogDO domain);

    Integer saveBatch(List<LogDO> domains);

    Integer updateById(LogDO domain);

    Integer removeById(Long id);

    LogDO getById(Long id);

    List<LogDO> listByParam(LogQueryParam param);

    Long countByParam(LogQueryParam param);
}
