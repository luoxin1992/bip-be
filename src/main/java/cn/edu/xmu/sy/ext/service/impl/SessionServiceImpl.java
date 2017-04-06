/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SessionMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SessionStatusEnum;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionForceOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
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
import java.util.Optional;
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
    public void online(SessionOnlineParam param) {
        Optional<Long> counterId = counterService.getIdByMacAndIpOptional(param.getMac(), param.getIp());
        if (!counterId.isPresent()) {
            logger.error("counter with mac {} and ip {} not exist", param.getMac(), param.getIp());
            throw new BizException(BizResultEnum.SESSION_COUNTER_NOT_EXIST);
        }

        //TODO 并发错误
        //不允许同一柜台多个客户端同时上线
        checkOnlineSessionExist(counterId.get());
        checkMqQueueExist(param.getQueue());

        //记录会话信息
        SessionDO domain = POJOConvertUtil.convert(param, SessionDO.class);
        domain.setCounterId(counterId.get());
        domain.setStatus(SessionStatusEnum.ONLINE.getStatus());
        domain.setOnlineTime(DateTimeUtil.getNow());
        if (sessionMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("session {} for counter {} online failed", domain.getId(), counterId.get());
            throw new BizException(BizResultEnum.SESSION_ONLINE_ERROR);
        }

        logService.logSessionOnline();
        logger.info("session {} for counter {} online successfully", domain.getId(), counterId.get());
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        Long sessionId = sessionMapper.getOnlineIdByQueue(param.getQueue());
        if (sessionId == null) {
            logger.error("online session with queue {} not exist", param.getQueue());
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_NOT_EXIST, param.getQueue());
        }

        SessionDO domain = new SessionDO();
        domain.setId(sessionId);
        domain.setStatus(SessionStatusEnum.OFFLINE.getStatus());
        domain.setOfflineTime(DateTimeUtil.getNow());
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.info("session {} offline failed", sessionId);
            throw new BizException(BizResultEnum.SESSION_OFFLINE_ERROR);
        }

        logService.logSessionOffline();
        logger.info("session {} offline successfully", sessionId);
    }

    @Override
    public void forceOffline(SessionForceOfflineParam param) {
        SessionDO session = sessionMapper.getById(param.getId());
        if (session == null) {
            logger.error("online session {} not exist", param.getId());
            throw new BizException(BizResultEnum.SESSION_NOT_EXIST, param.getId());
        }
        if (session.getStatus() != SessionStatusEnum.ONLINE.getStatus()) {
            logger.warn("session {} status not online", param.getId());
            return;
        }

        SessionDO domain = new SessionDO();
        domain.setId(param.getId());
        domain.setStatus(SessionStatusEnum.FORCE.getStatus());
        domain.setOfflineTime(DateTimeUtil.getNow());
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.info("session {} force offline failed", param.getId());
            throw new BizException(BizResultEnum.SESSION_FORCE_OFFLINE_ERROR);
        }

        logService.logSessionOffline();
        logger.info("session {} force offline successfully", param.getId());
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
            logger.warn("session query result is empty");
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

    /**
     * 检查队列是否已在MQ中注册
     *
     * @param queue 消息队列名称
     */
    private void checkMqQueueExist(String queue) {
        //TODO
    }
}
