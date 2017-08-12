/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.MessageDO;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface MessageMapper {
    Integer save(MessageDO domain);

    Integer saveBatch(List<MessageDO> domains);

    Integer updateById(MessageDO domain);

    Integer removeById(Long id);

    Integer removeByCounterId(Long counterId);

    Integer removeBySessionId(@Param("counterId") Long counterId, @Param("sessionId") Long sessionId);

    MessageDO getById(Long id);

    List<MessageDO> getByCounterId(Long counterId);

    List<MessageDO> getByUid(@Param("uid") Long uid, @Param("direction") Integer direction);

    List<MessageDO> listByUid(@Param("uids") List<Long> uids, @Param("direction") Integer direction);

    Long countByParam(MessageQueryParam param);

    List<MessageDO> listByParam(MessageQueryParam param);
}
