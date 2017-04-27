/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.param.BasePeriodParam;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.dto.DiffFieldDTO;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.CounterMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterDeleteParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SessionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (counterMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_CREATE_ERROR);
        }

        logService.logCounterCreate(domain);
        logger.info("create counter {} successfully", domain.getId());
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
        if (counterMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_MODIFY_ERROR);
        }

        //修改后处理过程
        modifyPostProcess(param.getId(), before, domain);

        logService.logCounterModify(before, domain);
        logger.info("modify counter {} successfully", domain.getId());
    }

    @Override
    public void delete(CounterDeleteParam param) {
        //仅能删除没有在线会话的柜台
        checkOnlineSessionExist(param.getId());

        if (counterMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete counter {} failed", param.getId());
            throw new BizException(BizResultEnum.COUNTER_DELETE_ERROR);
        }

        logService.logCounterDelete(param.getId());
        logger.info("delete counter {} successfully", param.getId());
    }

    @Override
    public BasePagingResult<CounterQueryResult> query(CounterQueryParam param) {
        long count = counterMapper.countByParam(param);
        if (count == 0) {
            logger.warn("counter query result is empty");
            throw new BizException(BizResultEnum.COUNTER_EMPTY_QUERY_RESULT);
        }

        Map<Long, CounterQueryResult> counters = queryAndMap(param);
        if (!MapUtils.isEmpty(counters)) {
            Map<Long, List<SessionQueryResult>> sessions = queryTodaySession(new ArrayList<>(counters.keySet()));
            counters.values()
                    .forEach(counter -> counter.setSessions(sessions.get(counter.getId())));
            logger.info("query {} counter(s) with {} session(s)", counters.size(), sessions.size());
        }
        logger.info("query {}/{} counter(s)", counters.size(), count);

        BasePagingResult<CounterQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(new ArrayList<>(counters.values()));
        return result;
    }

    @Override
    public Optional<Long> getIdByMacAndIpOptional(String mac, String ip) {
        return Optional.ofNullable(counterMapper.getIdByMacAndIp(mac, ip, null));
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
            throw new BizException(BizResultEnum.COUNTER_NUMBER_DUPLICATE, number);
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
            throw new BizException(BizResultEnum.COUNTER_MAC_OR_IP_DUPLICATE, mac, ip);
        }
    }

    /**
     * 检查在线会话是否存在
     *
     * @param id 柜台ID
     */
    private void checkOnlineSessionExist(Long id) {
        Optional<Long> sessionId = sessionService.getOnlineSessionIdOptional(id);
        if (sessionId.isPresent()) {
            logger.error("online session for counter {} exist", id);
            throw new BizException(BizResultEnum.COUNTER_ONLINE_SESSION_EXIST);
        }
    }

    /**
     * 修改柜台后处理(仅限有在线会话时)
     *
     * @param id     柜台ID
     * @param before 修改前的柜台信息
     * @param after  修改后的柜台信息
     */
    private void modifyPostProcess(Long id, CounterDO before, CounterDO after) {
        Optional<Long> sessionId = sessionService.getOnlineSessionIdOptional(id);
        if (sessionId.isPresent()) {
            List<String> diffFieldNames = POJOCompareUtil.compare(CounterDO.class, before, after).stream()
                    .map(DiffFieldDTO::getFieldName)
                    .collect(Collectors.toList());

            boolean modifyMacOrIp = diffFieldNames.stream()
                    .anyMatch((fieldName) -> fieldName.equals("mac") || fieldName.equals("ip"));
            if (modifyMacOrIp) {
                //TODO 如果修改了MAC地址或IP地址：强制离线
                logger.info("force offline session because of mac or ip modified");
                return;
            }

            boolean modifyNumberOrName = diffFieldNames.stream()
                    .anyMatch((fieldName) -> fieldName.equals("number") || fieldName.equals("name"));
            if (modifyNumberOrName) {
                //TODO 如果修改了编号或名称：发送更新消息
                logger.info("update session info because of number or name modified");
            }
        }
    }

    /**
     * 查询柜台，Map出柜台ID
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private Map<Long, CounterQueryResult> queryAndMap(CounterQueryParam param) {
        List<CounterDO> domains = counterMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyMap();
        }
        return domains.stream()
                .map(domain -> POJOConvertUtil.convert(domain, CounterQueryResult.class))
                .collect(Collectors.toMap(CounterQueryResult::getId, result -> result, (newKey, ExistKey) -> newKey,
                        LinkedHashMap::new));
    }

    /**
     * 批量查询给定柜台“今天”的会话
     *
     * @param counterIds 柜台ID
     * @return 查询结果
     */
    private Map<Long, List<SessionQueryResult>> queryTodaySession(List<Long> counterIds) {
        BasePeriodParam period = new BasePeriodParam();
        period.setStart(DateTimeUtil.getTodayAtStart());
        period.setEnd(DateTimeUtil.getTodayAtEnd());

        SessionBatchQueryParam param = new SessionBatchQueryParam();
        param.setCounterIds(counterIds);
        param.setPeriod(period);

        return sessionService.queryBatchAndGroup(param);
    }
}
