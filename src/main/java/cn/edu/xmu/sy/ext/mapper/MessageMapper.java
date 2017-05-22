/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.MessageDO;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import org.apache.ibatis.annotations.Mapper;

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

    Integer removeBySessionId(Long sessionId);

    MessageDO getById(Long id);

    MessageDO getByReplyId(Long replyId);

    List<MessageDO> getByCounterId(Long counterId);

    List<MessageDO> getBySessionId(Long sessionId);

    List<MessageDO> listByReplyId(List<Long> replyIds);

    List<MessageDO> listByCounterId(List<Long> counterIds);

    List<MessageDO> listBySessionId(List<Long> sessionIds);

    Long countByParam(MessageQueryParam param);

    List<MessageDO> listByParam(MessageQueryParam param);
}
