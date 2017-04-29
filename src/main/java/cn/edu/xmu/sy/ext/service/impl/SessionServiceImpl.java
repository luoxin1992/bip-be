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
import cn.edu.xmu.sy.ext.param.SessionLostParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
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
    private LogService logService;

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    @Transactional
    public SessionOnlineResult online(SessionOnlineParam param) {
        Optional<Long> counterId = counterService.getIdByMacAndIpOptional(param.getMac(), param.getIp());
        if (!counterId.isPresent()) {
            logger.error("counter with mac {} and ip {} not exist", param.getMac(), param.getIp());
            throw new BizException(BizResultEnum.SESSION_COUNTER_UNBOUNDED);
        }

        //不允许同一柜台多个客户端同时上线
        checkOnlineSessionExist(counterId.get());
        //生成Token，注册到消息服务
        //TODO 未考虑UUID重复
        //TODO 注册到消息服务
        String token = UUIDUtil.randomShortUUID();

        //记录会话信息
        SessionDO domain = POJOConvertUtil.convert(param, SessionDO.class);
        domain.setCounterId(counterId.get());
        domain.setToken(token);
        domain.setStatus(SessionStatusEnum.ONLINE.getStatus());
        domain.setOnlineTime(DateTimeUtil.getNow());
        if (sessionMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("session {} for counter {} online failed", domain.getId(), counterId.get());
            throw new BizException(BizResultEnum.SESSION_ONLINE_ERROR);
        }

        logService.logSessionOnline(domain);
        logger.info("session {} for counter {} online successfully", domain.getId(), counterId.get());

        SessionOnlineResult result = new SessionOnlineResult();
        result.setToken(token);
        return result;
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        Long sessionId = sessionMapper.getOnlineIdByToken(param.getToken());
        if (sessionId == null) {
            logger.error("online session with token {} not exist", param.getToken());
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_NOT_EXIST, param.getToken());
        }

        //TODO 从消息服务解除注册

        SessionDO domain = new SessionDO();
        domain.setId(sessionId);
        domain.setStatus(SessionStatusEnum.OFFLINE.getStatus());
        domain.setOfflineTime(DateTimeUtil.getNow());
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.info("session {} offline failed", sessionId);
            throw new BizException(BizResultEnum.SESSION_OFFLINE_ERROR);
        }

        logService.logSessionOffline(domain);
        logger.info("session {} offline successfully", sessionId);
    }

    @Override
    public void lost(SessionLostParam param) {
        Long sessionId = sessionMapper.getOnlineIdByToken(param.getToken());
        if (sessionId == null) {
            logger.error("online session with token {} not exist", param.getToken());
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_NOT_EXIST, param.getToken());
        }

        SessionDO domain = new SessionDO();
        domain.setId(sessionId);
        domain.setStatus(SessionStatusEnum.LOST.getStatus());
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.info("mark session {} status as lost failed", sessionId);
            throw new BizException(BizResultEnum.SESSION_LOST_ERROR);
        }

        logService.logSessionLost(domain);
        logger.info("mark session {} status as lost successfully", sessionId);
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
     * 检查柜台是否已存在在线会话
     *
     * @param counterId 柜台ID
     */
    private void checkOnlineSessionExist(Long counterId) {
        Long id = sessionMapper.getOnlineIdByCounterId(counterId);
        if (id != null) {
            logger.error("another session {} is online for counter {}", id, counterId);
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_EXIST);
        }
    }
}
