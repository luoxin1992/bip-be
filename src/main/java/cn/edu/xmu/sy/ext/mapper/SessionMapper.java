/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import org.apache.ibatis.annotations.Mapper;

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

    List<SessionDO> getByCounterId(Long counterId);

    List<SessionDO> listByCounterId(List<Long> counterIds);

    List<SessionDO> getByParam(SessionQueryParam param);

    List<SessionDO> listByParam(SessionBatchQueryParam param);

    Long getOnlineIdByCounterId(Long counterId);

    Long getIdByToken(String token);
}
