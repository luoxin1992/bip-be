/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SessionMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SessionStatusEnum;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.SessionService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-24
 */
@Service
public class SessionServiceImpl implements SessionService {
    private final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private CounterService counterService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LogService logService;

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    @Transactional
    public SessionOnlineResult online(SessionOnlineParam param) {
        CounterQueryResult counter = checkCounterBind(param.getMac(), param.getIp());
        //不允许同一柜台多个客户端同时上线
        checkOnlineSessionNotExist(counter.getId());
        //生成Token
        String token = UUIDUtil.randomShortUUID();

        //记录会话信息
        SessionDO domain = new SessionDO();
        domain.setCounterId(counter.getId());
        domain.setToken(token);
        domain.setStatus(SessionStatusEnum.ONLINE.getStatus());
        domain.setOnlineTime(DateTimeUtil.getNow());
        if (sessionMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("session {} for counter {} online failed", domain.getId(), counter.getId());
            throw new BizException(BizResultEnum.SESSION_ONLINE_ERROR);
        }

        //注册到消息服务
        messageService.registerSession(domain.getId(), token);

        logService.logSessionOnline(domain.getId(), token);
        logger.info("session {} for counter {} online successfully", domain.getId(), counter.getId());

        SessionOnlineResult result = new SessionOnlineResult();
        result.setToken(token);
        result.setNumber(counter.getNumber());
        result.setName(counter.getName());
        return result;
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        Long id = sessionMapper.getIdByToken(param.getToken());
        if (id == null) {
            logger.error("session with token {} not exist", param.getToken());
            throw new BizException(BizResultEnum.SESSION_NOT_EXIST, param.getToken());
        }

        checkSessionIsOnline(id);
        //从消息服务解除注册
        messageService.unregisterSession(id);
        updateSessionStatus(id, SessionStatusEnum.OFFLINE);

        logService.logSessionOffline(id);
        logger.info("session {} offline successfully", id);
    }

    @Override
    @Transactional
    public void lost(Long id) {
        checkSessionIsOnline(id);
        updateSessionStatus(id, SessionStatusEnum.LOST);
        logger.info("mark session {} status as lost successfully", id);
    }

    @Override
    @Transactional
    public void close(Long id) {
        checkSessionIsOnline(id);
        updateSessionStatus(id, SessionStatusEnum.CLOSE);
        logger.info("mark session {} status as close successfully", id);
    }

    @Override
    public List<SessionQueryResult> query(SessionQueryParam param) {
        List<SessionDO> domains = sessionMapper.getByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session query result is empty");
            return Collections.emptyList();
        }

        logger.info("query {} session(s) for counter {}", domains.size(), param.getCounterId());
        return domains.stream()
                .map(domain -> {
                    SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
                    result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SessionQueryResult> queryBatch(SessionBatchQueryParam param) {
        List<SessionDO> domains = sessionMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session batch query result is empty");
            return Collections.emptyList();
        }

        logger.info("batch query {} sessions for {} counters", domains.size(), param.getCounterIds().size());
        return domains.stream()
                .map(domain -> {
                    SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
                    result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<SessionQueryResult>> queryBatchAndGroup(SessionBatchQueryParam param) {
        List<SessionQueryResult> sessions = queryBatch(param);
        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyMap();
        }
        return sessions.stream()
                .collect(Collectors.groupingBy(SessionQueryResult::getCounterId, TreeMap::new, Collectors.toList()));
    }

    @Override
    public Optional<Long> getOnlineSessionIdOptional(Long counterId) {
        return Optional.ofNullable(sessionMapper.getOnlineIdByCounterId(counterId));
    }

    /**
     * 根据会话上线参数(MAC+IP)查询是否已绑定(即存在)某个柜台
     *
     * @param mac MAC地址
     * @param ip  IP地址
     * @return 查询结果
     */
    private CounterQueryResult checkCounterBind(String mac, String ip) {
        Optional<CounterQueryResult> result = counterService.getByMacAndIpOptional(mac, ip);
        if (!result.isPresent()) {
            logger.error("counter with mac {} and ip {} unbound", mac, ip);
            throw new BizException(BizResultEnum.SESSION_COUNTER_UNBOUNDED);
        }
        return result.get();
    }

    /**
     * 检查柜台是否不存在在线会话
     *
     * @param counterId 柜台ID
     */
    private void checkOnlineSessionNotExist(Long counterId) {
        Long id = sessionMapper.getOnlineIdByCounterId(counterId);
        if (id != null) {
            logger.error("another session {} is online for counter {}", id, counterId);
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_EXIST);
        }
    }

    /**
     * 检查会话是否不存在在线会话
     *
     * @param sessionId 会话ID
     */
    private void checkSessionIsOnline(Long sessionId) {
        SessionDO domain = sessionMapper.getById(sessionId);
        if (domain.getStatus() != SessionStatusEnum.ONLINE.getStatus()) {
            logger.error("session {] not online", sessionId);
            throw new BizException(BizResultEnum.SESSION_NOT_ONLINE, sessionId);
        }
    }

    private void updateSessionStatus(Long id, SessionStatusEnum status) {
        SessionDO domain = new SessionDO();
        domain.setId(id);
        domain.setStatus(status.getStatus());
        if (status == SessionStatusEnum.OFFLINE) {
            domain.setOfflineTime(DateTimeUtil.getNow());
        }
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.info("update session {} status failed", id);
            throw new BizException(BizResultEnum.SESSION_UPDATE_STATUS_ERROR);
        }
    }
}
