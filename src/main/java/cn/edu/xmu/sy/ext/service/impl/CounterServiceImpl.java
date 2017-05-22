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
import cn.edu.xmu.sy.ext.param.MessageCloseParam;
import cn.edu.xmu.sy.ext.param.MessageCounterInfoParam;
import cn.edu.xmu.sy.ext.param.MessageSendToParam;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.MessageService;
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
    private MessageService messageService;
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

        logService.logCounterCreate(domain.getId(), domain.getNumber(), domain.getName());
        logger.info("create counter {} successfully", domain.getId());
    }

    @Override
    @Transactional
    public void modify(CounterModifyParam param) {
        //获取修改前窗口信息
        CounterDO before = counterMapper.getById(param.getId());
        if (before == null) {
            logger.error("counter {} not exist", param.getId());
            throw new BizException(BizResultEnum.COUNTER_NOT_EXIST, param.getId());
        }

        //编号不能与除自身外的重复
        checkNumberDuplicate(param.getNumber(), param.getId());
        //MAC+IP不能与除自身外的重复
        checkMacOrIpDuplicate(param.getMac(), param.getIp(), param.getId());

        CounterDO after = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify counter {} failed", after.getId());
            throw new BizException(BizResultEnum.COUNTER_MODIFY_ERROR);
        }

        //修改后处理过程
        modifyPostProcess(param.getId(), before, after);

        logService.logCounterModify(after.getId(), POJOCompareUtil.compare(CounterDO.class, before, after));
        logger.info("modify counter {} successfully", after.getId());
    }

    @Override
    public void delete(CounterDeleteParam param) {
        //仅能删除没有在线会话的窗口
        checkOnlineSessionNotExist(param.getId());

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
            return new BasePagingResult<>();
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
    public CounterQuerySimpleResult querySimple(String mac, String ip) {
        CounterDO domain = counterMapper.getByMacAndIp(mac, ip);
        return domain != null ? POJOConvertUtil.convert(domain, CounterQuerySimpleResult.class) : null;
    }

    @Override
    public Optional<Long> getIdByNumberOptional(String number) {
        return Optional.ofNullable(counterMapper.getIdByNumber(number, null));
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
        Long id = counterMapper.getIdByMacOrIp(mac, ip, exclude);
        if (id != null) {
            logger.error("counter mac {} or ip {} duplicate", mac, ip);
            throw new BizException(BizResultEnum.COUNTER_MAC_OR_IP_DUPLICATE, mac, ip);
        }
    }

    /**
     * 检查在线会话是否存在
     *
     * @param id 窗口ID
     */
    private void checkOnlineSessionNotExist(Long id) {
        Optional<Long> sessionId = sessionService.getOnlineIdByCounterIdOptional(id);
        if (sessionId.isPresent()) {
            logger.error("online session for counter {} exist", id);
            throw new BizException(BizResultEnum.COUNTER_ONLINE_SESSION_EXIST);
        }
    }

    /**
     * 修改窗口后处理(仅限有在线会话时)
     *
     * @param id     窗口ID
     * @param before 修改前的窗口信息
     * @param after  修改后的窗口信息
     */
    private void modifyPostProcess(Long id, CounterDO before, CounterDO after) {
        Optional<Long> sessionId = sessionService.getOnlineIdByCounterIdOptional(id);
        if (sessionId.isPresent()) {
            List<String> diffFieldNames = POJOCompareUtil.compare(CounterDO.class, before, after).stream()
                    .map(DiffFieldDTO::getFieldName)
                    .collect(Collectors.toList());

            boolean modifyMacOrIp = diffFieldNames.stream()
                    .anyMatch((fieldName) -> fieldName.equals("mac") || fieldName.equals("ip"));
            if (modifyMacOrIp) {
                //如果修改了MAC地址或IP地址：强制离线
                MessageSendToParam sendTo = new MessageSendToParam();
                sendTo.setId(after.getId());
                MessageCloseParam param = new MessageCloseParam();
                param.setSendTo(sendTo);

                messageService.sendClose(param);
                logger.info("force offline session because of mac or ip modified");
                return;
            }

            boolean modifyNumberOrName = diffFieldNames.stream()
                    .anyMatch((fieldName) -> fieldName.equals("number") || fieldName.equals("name"));
            if (modifyNumberOrName) {
                //如果修改了编号或名称：发送更新消息
                MessageSendToParam sendTo = new MessageSendToParam();
                sendTo.setId(after.getId());
                MessageCounterInfoParam param = new MessageCounterInfoParam();
                param.setSendTo(sendTo);
                param.setNumber(after.getNumber());
                param.setName(after.getName());

                messageService.sendCounterInfo(param);
                logger.info("update counter info because of number or name modified");
            }
        }
    }

    /**
     * 查询窗口，Map出窗口ID
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
     * 批量查询给定窗口“今天”的会话
     *
     * @param counterIds 窗口ID
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
