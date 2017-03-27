/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.param.BasePeriodParam;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.CounterMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.CounterAutoCreateParam;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@Service
public class CounterServiceImpl implements CounterService {
    private final Logger logger = LoggerFactory.getLogger(CounterServiceImpl.class);

    @Autowired
    private SessionService sessionService;
    @Autowired
    private LogService logService;

    @Autowired
    private CounterMapper counterMapper;

    @Override
    @Transactional
    public void create(CounterCreateParam param) {
        //编号不能重复
        checkNumberDuplicate(param.getNumber(), null);
        //MAC+IP不能重复
        checkMacOrIpDuplicate(param.getMac(), param.getIp(), null);

        CounterDO domain = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.save(domain) != 1) {
            logger.error("create counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_CREATE_ERROR);
        }

        logService.logCounterCreate(domain);
        logger.info("create counter {} successfully", domain.getId());
    }

    @Override
    @Transactional
    public void autoCreate(CounterAutoCreateParam param) {
        //自动创建柜台的入口为第一次上线的新柜台，入参已校验过
        CounterDO domain = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.save(domain) != 1) {
            logger.error("create counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_AUTO_CREATE_ERROR);
        }

        logService.logCounterAutoCreate(domain);
        logger.info("auto create counter {} successfully", domain.getId());
    }

    @Override
    @Transactional
    public void modify(CounterModifyParam param) {
        //获取修改前柜台信息
        CounterDO before = counterMapper.getById(param.getId());
        if (before == null) {
            logger.error("counter {} not exist", param.getId());
            throw new BizException(BizResultEnum.COUNTER_NOT_EXIST, param.getId());
        }
        //编号不能与除自身外的重复
        checkNumberDuplicate(param.getNumber(), param.getId());
        //MAC+IP不能与除自身外的重复
        checkMacOrIpDuplicate(param.getMac(), param.getIp(), param.getId());

        CounterDO domain = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.updateById(domain) != 1) {
            logger.error("modify counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_MODIFY_ERROR);
        }

        logService.logCounterModify(before, domain);
        logger.info("modify counter {} successfully", domain.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        //仅能删除没有在线会话的柜台
        checkOnlineSessionExist(id);

        if (counterMapper.removeById(id) != 1) {
            logger.error("delete counter {} failed", id);
            throw new BizException(BizResultEnum.COUNTER_DELETE_ERROR);
        }

        logService.logCounterDelete(id);
        logger.info("delete counter {} successfully", id);
    }

    @Override
    public BasePagingResult<CounterQueryResult> query(CounterQueryParam param) {
        long count = counterMapper.countByParam(param);
        Map<Long, CounterQueryResult> counters = queryCounter(param);
        Map<Long, List<SessionQueryResult>> sessions = querySession(new ArrayList<>(counters.keySet()));
        for (CounterQueryResult result : counters.values()) {
            result.setSessions(sessions.get(result.getId()));
        }

        BasePagingResult<CounterQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(new ArrayList<>(counters.values()));
        return result;
    }

    @Override
    public Long getIdByMacAndIp(String mac, String ip) {
        return counterMapper.getIdByMacAndIp(mac, ip, null);
    }

    /**
     * 根据ID查询柜台
     * 若不存在则抛出异常
     *
     * @param id 柜台ID
     * @return 柜台信息
     */
    private CounterDO getById(Long id) {
        CounterDO domain = counterMapper.getById(id);
        if (domain == null) {
            logger.error("counter {} not exist", id);
            throw new BizException(BizResultEnum.COUNTER_NOT_EXIST, id);
        }
        return domain;
    }

    /**
     * 检查编号是否重复
     *
     * @param number  编号
     * @param exclude 排除ID
     */
    private void checkNumberDuplicate(String number, Long exclude) {
        Long id = counterMapper.getIdByNumber(number, exclude);
        if (id != null) {
            logger.error("counter number {} duplicate", number);
            throw new BizException(BizResultEnum.COUNTER_NUMBER_DUPLICATE);
        }
    }

    /**
     * 检查MAC地址或IP地址是否重复
     *
     * @param mac     MAC地址
     * @param ip      IP地址
     * @param exclude 排除ID
     */
    private void checkMacOrIpDuplicate(String mac, String ip, Long exclude) {
        Long id = counterMapper.getIdByMacAndIp(mac, ip, exclude);
        if (id != null) {
            logger.error("counter mac {} or ip {} duplicate", mac, ip);
            throw new BizException(BizResultEnum.COUNTER_MAC_OR_IP_DUPLICATE);
        }
    }

    /**
     * 检查在线会话是否存在
     *
     * @param id 柜台ID
     */
    private void checkOnlineSessionExist(Long id) {
        Long sessionId = sessionService.getOnlineSessionId(id);
        if (sessionId != null) {
            logger.error("online session for counter {} exist", id);
            throw new BizException(BizResultEnum.COUNTER_ONLINE_SESSION_EXIST);
        }
    }

    /**
     * 查询柜台，返回柜台ID-查询结果的Map
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private Map<Long, CounterQueryResult> queryCounter(CounterQueryParam param) {
        Map<Long, CounterQueryResult> retMap = new LinkedHashMap<>();
        List<CounterDO> domains = counterMapper.listByParam(param);
        for (CounterDO domain : domains) {
            retMap.put(domain.getId(), POJOConvertUtil.convert(domain, CounterQueryResult.class));
        }
        return retMap;
    }

    /**
     * 查询会话，返回柜台ID-查询结果的Map
     *
     * @param counterIds 柜台ID
     * @return 查询结果
     */
    private Map<Long, List<SessionQueryResult>> querySession(List<Long> counterIds) {
        SessionBatchQueryParam param = new SessionBatchQueryParam();
        param.setCounterIds(counterIds);
        param.setPeriod(new BasePeriodParam(DateTimeUtil.getTodayStr(DateTimeConstant.DATE_PATTERN)));
        List<SessionQueryResult> sessions = sessionService.queryBatch(param);

        if (CollectionUtils.isEmpty(sessions)) {
            return Collections.emptyMap();
        }
        Map<Long, List<SessionQueryResult>> retMap = new LinkedHashMap<>();
        for (SessionQueryResult session : sessions) {
            if (!retMap.containsKey(session.getCounterId())) {
                retMap.put(session.getCounterId(), new ArrayList<>());
            }
            retMap.get(session.getCounterId()).add(session);
        }
        return retMap;
    }
}
