/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.CounterMapper;
import cn.edu.xmu.sy.ext.mapper.SessionMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SessionStatusEnum;
import cn.edu.xmu.sy.ext.param.CounterAutoCreateParam;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SessionService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private CounterMapper counterMapper;
    @Autowired
    private SessionMapper sessionMapper;

    @Override
    @Transactional
    public void online(SessionOnlineParam param) {
        Long counterId = counterService.getIdByMacAndIp(param.getMac(), param.getIp());
        if (counterId == null) {
            //新柜台首次上线，自动创建
            counterService.autoCreate(POJOConvertUtil.convert(param, CounterAutoCreateParam.class));
        } else {
            //已有柜台，不允许多个客户端同时上线
            checkOnlineSessionExist(counterId);
        }
        //TODO 检查队列名称是否已在MQ服务中注册
        //记录会话信息
        SessionDO domain = POJOConvertUtil.convert(param, SessionDO.class);
        domain.setCounterId(counterId);
        domain.setOnlineTime(LocalDateTime.now());
        sessionMapper.save(domain);

        logService.logSessionOnline();
        logger.info("session {} for counter {} online successfully", domain.getId(), counterId);
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        Long counterId = counterService.getIdByMacAndIp(param.getMac(), param.getIp());
        if (counterId == null) {
            //不存在此柜台，无法下线
            logger.error("fail to offline session, counter not exist");
            throw new BizException(BizResultEnum.SESSION_OFFLINE_COUNTER_NOT_EXIST);
        }
        Long sessionId = getOnlineSessionId(counterId);
        if (sessionId == null) {
            //不存在在线会话，无法下线
            logger.error("fail to offline session, online session not exist");
            throw new BizException(BizResultEnum.SESSION_OFFLINE_COUNTER_NOT_EXIST);
        }
        //TODO 检查队列名称
        SessionDO domain = new SessionDO();
        domain.setId(sessionId);
        domain.setOfflineTime(LocalDateTime.now());
        sessionMapper.updateById(domain);

        logService.logSessionOffline();
        logger.info("session {} for counter {} offline successfully", sessionId, counterId);
    }

    @Override
    public List<SessionQueryResult> query(SessionQueryParam param) {
        List<SessionDO> domains = sessionMapper.getByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session query result is empty");
            return Collections.emptyList();
        }
        List<SessionQueryResult> results = new ArrayList<>();
        for (SessionDO domain : domains) {
            SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
            result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
            if (domain.getOnlineTime() != null) {
                result.setOnlineTime(DateTimeUtil.format(domain.getOnlineTime()));
            }
            //TODO 心跳时间
            if (domain.getOfflineTime() != null) {
                result.setOfflineTime(DateTimeUtil.format(domain.getOfflineTime()));
            }
            results.add(result);
        }
        logger.info("query {} sessions for counter {}", results.size(), param.getCounterId());
        return results;
    }

    @Override
    public List<SessionQueryResult> queryBatch(SessionBatchQueryParam param) {
        List<SessionDO> domains = sessionMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session query result is empty");
            return Collections.emptyList();
        }
        List<SessionQueryResult> results = new ArrayList<>();
        for (SessionDO domain : domains) {
            SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
            result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
            if (domain.getOnlineTime() != null) {
                result.setOnlineTime(DateTimeUtil.format(domain.getOnlineTime()));
            }
            //TODO 心跳时间
            if (domain.getOfflineTime() != null) {
                result.setOfflineTime(DateTimeUtil.format(domain.getOfflineTime()));
            }
            results.add(result);
        }
        logger.info("batch query {} sessions for {} counters", results.size(), param.getCounterIds().size());
        return results;
    }

    @Override
    public Long getOnlineSessionId(Long counterId) {
        return sessionMapper.getOnlineSessionId(counterId);
    }

    private void checkOnlineSessionExist(Long counterId) {
        Long id = sessionMapper.getOnlineSessionId(counterId);
        if (id != null) {
            logger.error("another session {} is online for counter {}", id, counterId);
            throw new BizException(BizResultEnum.SESSION_ONLINE_SESSION_EXIST);
        }
    }
}
