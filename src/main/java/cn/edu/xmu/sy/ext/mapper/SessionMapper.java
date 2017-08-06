/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SessionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会话Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface SessionMapper {
    Integer save(SessionDO domain);

    Integer saveBatch(List<SessionDO> domains);

    Integer updateById(SessionDO domain);

    Integer removeById(Long id);

    Integer removeByCounterId(Long counterId);

    SessionDO getById(Long id);

    SessionDO getByToken(String token);

    List<SessionDO> listById(List<Long> ids);

    List<SessionDO> getByCounterId(@Param("counterId") Long counterId, @Param("limit") Integer limit);

    List<SessionDO> listByCounterId(List<Long> counterIds);
}
