/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

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
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionCloseParam;
import cn.edu.xmu.sy.ext.param.SessionLostClientParam;
import cn.edu.xmu.sy.ext.param.SessionLostServerParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;
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
        //查询绑定的窗口
        CounterQuerySimpleResult counter = checkCounterBind(param.getMac(), param.getIp());
        //不允许同一窗口多个客户端同时上线
        checkOnlineSessionNotExist(counter.getId());
        //生成Token
        String token = generateToken(counter.getId());

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

        //注册到Msg服务
        messageService.sendTokenRegister(token);

        logService.logSessionOnline(domain.getCounterId(), domain.getId(), domain.getToken());
        logger.info("session {} for counter {} online successfully", domain.getId(), counter.getId());

        SessionOnlineResult result = new SessionOnlineResult();
        result.setToken(token);
        result.setCounter(counter);
        return result;
    }

    @Override
    @Transactional
    public void offline(SessionOfflineParam param) {
        Long id = checkTokenExist(param.getToken());
        checkSessionIsOnline(id);

        //从消息服务解除注册
        messageService.sendTokenUnregister(param.getToken());
        //更新会话状态
        updateSessionStatus(id, SessionStatusEnum.OFFLINE);

        logService.logSessionOffline(id, param.getToken());
        logger.info("session {} offline successfully", id);
    }

    @Override
    @Transactional
    public void lostServer(SessionLostServerParam param) {
        Long id = checkTokenExist(param.getToken());
        checkSessionIsOnline(id);

        updateSessionStatus(id, SessionStatusEnum.LOST_SERVER);

        logService.logSessionLostServer(id, param.getToken());
        logger.info("mark session {} status as server lost successfully", id);
    }

    @Override
    @Transactional
    public void lostClient(SessionLostClientParam param) {
        Long id = checkTokenExist(param.getToken());
        checkSessionIsOnline(id);

        updateSessionStatus(id, SessionStatusEnum.LOST_CLIENT);

        logService.logSessionLostServer(id, param.getToken());
        logger.info("mark session {} status as client lost successfully", id);
    }

    @Override
    @Transactional
    public void close(SessionCloseParam param) {
        checkSessionIsOnline(param.getId());

        //TODO 发送关闭消息
        updateSessionStatus(param.getId(), SessionStatusEnum.CLOSE);

        logService.logSessionClose(param.getId());
        logger.info("mark session {} status as close successfully", param.getId());
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

        logger.info("batch query {} session(s) for {} counter(s)", domains.size(), param.getCounterIds().size());
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
     * 根据会话上线参数(MAC+IP)查询是否已绑定(存在)某个窗口
     *
     * @param mac MAC地址
     * @param ip  IP地址
     * @return 查询结果
     */
    private CounterQuerySimpleResult checkCounterBind(String mac, String ip) {
        CounterQuerySimpleResult result = counterService.querySimple(mac, ip);
        if (result == null) {
            logger.error("counter with mac {} and ip {} unbind", mac, ip);
            throw new BizException(BizResultEnum.SESSION_COUNTER_UNBIND);
        }
        return result;
    }

    /**
     * 检查窗口是否不存在在线会话
     *
     * @param counterId 窗口ID
     */
    private void checkOnlineSessionNotExist(Long counterId) {
        Long id = sessionMapper.getOnlineIdByCounterId(counterId);
        if (id != null) {
            logger.error("another session {} is online for counter {}", id, counterId);
            throw new BizException(BizResultEnum.SESSION_ANOTHER_ONLINE);
        }
    }

    /**
     * 生成Token
     * 规则：MD5(窗口ID+当前时间+盐)
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
     * 检查会话Token是否存在
     *
     * @param token Token
     * @return 会话ID
     */
    private Long checkTokenExist(String token) {
        Long id = sessionMapper.getIdByToken(token);
        if (id == null) {
            logger.error("session with token {} not exist", token);
            throw new BizException(BizResultEnum.SESSION_TOKEN_NOT_EXIST, token);
        }
        return id;
    }

    /**
     * 检查会话是否在线
     *
     * @param id 会话ID
     */
    private void checkSessionIsOnline(Long id) {
        SessionDO domain = sessionMapper.getById(id);
        if (domain.getStatus() != SessionStatusEnum.ONLINE.getStatus()) {
            logger.error("session {] is not online", id);
            throw new BizException(BizResultEnum.SESSION_NOT_ONLINE, id);
        }
    }

    private void updateSessionStatus(Long id, SessionStatusEnum status) {
        SessionDO domain = new SessionDO();
        domain.setId(id);
        domain.setStatus(status.getStatus());
        //客户端请求离线、管理后台强制关闭都视为正常情况，记录当前时间
        if (status == SessionStatusEnum.OFFLINE || status == SessionStatusEnum.CLOSE) {
            domain.setOfflineTime(DateTimeUtil.getNow());
        }
        if (sessionMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("update session {} status to {} failed", id, status.getStatus());
            throw new BizException(BizResultEnum.SESSION_UPDATE_STATUS_ERROR);
        }
    }
}
