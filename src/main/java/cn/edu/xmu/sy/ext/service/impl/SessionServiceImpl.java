/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.DigestUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.SessionMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SessionStatusEnum;
import cn.edu.xmu.sy.ext.param.SessionKickParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.result.SessionQuerySimpleResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.WebSocketService;
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
@CatTransaction
@Service
public class SessionServiceImpl implements SessionService {
    private final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private CounterService counterService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private LogService logService;

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    @Transactional
    public SessionOnlineResult online(SessionOnlineParam param) {
        //不允许同一窗口多个客户端同时上线
        checkAnotherOnline(param.getCounterId());
        //生成Token
        String token = generateToken(param.getCounterId());

        //记录会话信息
        SessionDO domain = new SessionDO();
        domain.setCounterId(param.getCounterId());
        domain.setToken(token);
        domain.setStatus(SessionStatusEnum.ONLINE.getStatus());
        domain.setOnlineTime(DateTimeUtil.getNow());
        if (sessionMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("session {} for counter {} online failed", domain.getId(), param.getCounterId());
            throw new BizException(BizResultEnum.SESSION_ONLINE_ERROR);
        }

        logService.logSessionCreate(domain.getId(), domain.getCounterId(), domain.getToken());
        logger.info("create session {} for counter {}", domain.getId(), param.getCounterId());

        SessionOnlineResult result = new SessionOnlineResult();
        result.setToken(token);
        return result;
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        SessionDO domain = getByToken(param.getToken());
        checkStatusOnline(domain);
        updateStatus(domain.getId(), SessionStatusEnum.OFFLINE);
    }

    @Override
    @Transactional
    public void kick(SessionKickParam param) {
        SessionDO domain = getById(param.getId());
        checkStatusOnline(domain);
        updateStatus(param.getId(), SessionStatusEnum.KICK);
        //强制关闭WebSocket连接
        webSocketService.closeSession(domain.getToken());
    }

    @Override
    @Transactional
    public void lost(String token) {
        SessionDO domain = getByToken(token);
        checkStatusOnline(domain);
        updateStatus(domain.getId(), SessionStatusEnum.LOST);
    }

    @Override
    public void verify(String token) {
        SessionDO domain = getByToken(token);
        checkStatusOnline(domain);
    }

    @Override
    public void deleteByCounter(Long counterId) {
        List<SessionDO> domains = sessionMapper.getByCounterId(counterId, null);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("no session for counter {}", counterId);
            return;
        }

        //不能删除在线会话
        domains.stream()
                .filter(domain -> domain.getStatus() == SessionStatusEnum.ONLINE.getStatus())
                .findAny()
                .ifPresent(domain -> {
                    logger.error("online session {} can not delete", domain.getId());
                    throw new BizException(BizResultEnum.SESSION_DELETE_ONLINE);
                });

        if (sessionMapper.removeByCounterId(counterId) != domains.size()) {
            logger.error("delete session for counter {} failed", counterId);
            throw new BizException(BizResultEnum.SESSION_DELETE_ERROR);
        }

        logService.logSessionDeleteByCounter(counterId);
        logger.info("delete {} session(s) for counter {}", domains.size(), counterId);
    }

    @Override
    public BaseListResult<SessionQueryResult> query(SessionQueryParam param) {
        //同一个窗口可能有非常多会话记录，查询时限制返回长度
        List<SessionDO> domains = sessionMapper.getByCounterId(param.getCounterId(), param.getLimit());
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session query result is empty");
            return new BaseListResult<>();
        }

        List<SessionQueryResult> results = domains.stream()
                .map(domain -> {
                    SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
                    result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("query {} session(s) for counter {}", domains.size(), param.getCounterId());

        BaseListResult<SessionQueryResult> result = new BaseListResult<>();
        result.setTotal(results.size());
        result.setList(results);
        return result;
    }

    @Override
    public Optional<SessionQuerySimpleResult> queryOnline(Long counterId) {
        //在线会话只可能是最新的一个会话
        List<SessionDO> domains = sessionMapper.getByCounterId(counterId, 1);
        if (CollectionUtils.isEmpty(domains)) {
            return Optional.empty();
        } else {
            SessionDO domain = domains.get(0);
            return Optional.ofNullable(domain.getStatus() == SessionStatusEnum.ONLINE.getStatus() ?
                    POJOConvertUtil.convert(domain, SessionQuerySimpleResult.class) : null);
        }
    }

    @Override
    public SessionQuerySimpleResult queryIfOnline(Long sessionId) {
        SessionDO domain = sessionMapper.getById(sessionId);
        if (domain == null) {
            logger.error("session {} not exist", sessionId);
            throw new BizException(BizResultEnum.SESSION_NOT_EXIST, sessionId);
        }
        if (domain.getStatus() != SessionStatusEnum.ONLINE.getStatus()) {
            logger.error("session {} token is no longer online", sessionId);
            throw new BizException(BizResultEnum.SESSION_TOKEN_INVALIDATE, domain.getToken());
        }
        return POJOConvertUtil.convert(domain, SessionQuerySimpleResult.class);
    }

    @Override
    public List<SessionQuerySimpleResult> queryAllOnline() {
        List<CounterQuerySimpleResult> results = counterService.list().getList();
        return results.stream()
                .map(result -> queryOnline(result.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, SessionQueryResult> queryBatch(List<Long> sessionIds) {
        if (CollectionUtils.isEmpty(sessionIds)) {
            logger.warn("empty session id list in query param");
            return Collections.emptyMap();
        }

        List<SessionDO> domains = sessionMapper.listById(sessionIds);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session batch query result is empty");
            return Collections.emptyMap();
        }

        logger.info("batch query {} session(s)", domains.size());
        return domains.stream()
                .collect(Collectors.toMap(SessionDO::getId, domain -> {
                    SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
                    result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
                    return result;
                }));
    }

    @Override
    public Map<Long, List<SessionQueryResult>> queryBatchAndGroup(List<Long> counterIds) {
        if (CollectionUtils.isEmpty(counterIds)) {
            logger.warn("empty counter id list in query param");
            return Collections.emptyMap();
        }

        //批量查询时，每个窗口只会返回最新的1条会话记录
        List<SessionDO> domains = sessionMapper.listByCounterId(counterIds);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("session batch query by counter id result is empty");
            return Collections.emptyMap();
        }

        logger.info("batch query {} session(s) for {} counter(s)", domains.size(), counterIds.size());
        return domains.stream()
                .collect(Collectors.groupingBy(SessionDO::getCounterId, TreeMap::new,
                        Collectors.mapping(domain -> {
                            SessionQueryResult result = POJOConvertUtil.convert(domain, SessionQueryResult.class);
                            result.setStatus(SessionStatusEnum.getDescriptionByStatus(domain.getStatus()));
                            return result;
                        }, Collectors.toList())));
    }

    /**
     * 检查窗口是否存在其他在线会话
     *
     * @param counterId 窗口ID
     */
    private void checkAnotherOnline(Long counterId) {
        queryOnline(counterId).ifPresent(result -> {
            logger.error("another session {} is online for counter {}", result.getId(), counterId);
            throw new BizException(BizResultEnum.SESSION_ANOTHER_ONLINE);
        });
    }

    /**
     * 生成Token
     * 规则：MD5(窗口ID+当前时间+短UUID)
     *
     * @param counterId 窗口ID
     * @return Token
     */
    private String generateToken(Long counterId) {
        String token = counterId + DateTimeUtil.getNowStr(DateTimeConstant.DATETIME_PATTERN) +
                UUIDUtil.randomShortUUID();
        return DigestUtil.getStringMD5(token);
    }

    /**
     * 根据ID查询会话
     *
     * @param id 会话ID
     * @return 查询结果
     */
    private SessionDO getById(Long id) {
        SessionDO domain = sessionMapper.getById(id);
        if (domain == null) {
            logger.error("session {} not exist", id);
            throw new BizException(BizResultEnum.SESSION_NOT_EXIST, id);
        }
        return domain;
    }

    /**
     * 根据Token查询会话
     *
     * @param token Token
     * @return 查询结果
     */
    private SessionDO getByToken(String token) {
        SessionDO domain = sessionMapper.getByToken(token);
        if (domain == null) {
            logger.error("session with token {} not exist", token);
            throw new BizException(BizResultEnum.SESSION_TOKEN_NOT_EXIST, token);
        }
        return domain;
    }

    /**
     * 检查Session状态是否为在线
     *
     * @param domain 会话
     */
    private void checkStatusOnline(SessionDO domain) {
        if (domain.getStatus() != SessionStatusEnum.ONLINE.getStatus()) {
            logger.warn("session {} status is {} instead of online", domain.getId(), domain.getStatus());
            throw new BizException(BizResultEnum.SESSION_TOKEN_INVALIDATE, domain.getToken());
        }
    }

    /**
     * 更新会话状态
     *
     * @param id     会话ID
     * @param status 会话状态
     */
    private void updateStatus(Long id, SessionStatusEnum status) {
        SessionDO domain = new SessionDO();
        domain.setId(id);
        domain.setStatus(status.getStatus());
        //更新后的状态均为无效状态，统一记录为离线时间
        domain.setOfflineTime(DateTimeUtil.getNow());

        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("update session {} status to {} failed", domain.getId(), status.getStatus());
            throw new BizException(BizResultEnum.SESSION_UPDATE_STATUS_ERROR);
        }

        logService.logSessionUpdateStatus(id, status.getDescription());
        logger.info("update session {} status to {}", id, status.getStatus());
    }
}
