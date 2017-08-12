/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.CounterMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterDeleteParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryBindParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@CatTransaction
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
        //编号不能重复，MAC+IP不能重复
        checkNumberDuplicate(param.getNumber(), null);
        checkMacAndIpDuplicate(param.getMac(), param.getIp(), null);

        CounterDO domain = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create counter {} failed", domain.getId());
            throw new BizException(BizResultEnum.COUNTER_CREATE_ERROR);
        }

        logService.logCounterCreate(domain.getId(), domain.getNumber(), domain.getName());
        logger.info("create counter {}", domain.getId());
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

        //编号不能与除自身外的重复，MAC+IP不能与除自身外的重复
        checkNumberDuplicate(param.getNumber(), param.getId());
        checkMacAndIpDuplicate(param.getMac(), param.getIp(), param.getId());

        CounterDO after = POJOConvertUtil.convert(param, CounterDO.class);
        if (counterMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify counter {} failed", after.getId());
            throw new BizException(BizResultEnum.COUNTER_MODIFY_ERROR);
        }

        //如果窗口当前有在线会话，修改编号和名称后向其发送消息，不允许修改MAC和IP
        postponeWhenNumberOrNameModify(before, after);
        postponeWhenMacOrIpModify(before, after);

        logService.logCounterModify(after.getId(), POJOCompareUtil.compare(CounterDO.class, before, after));
        logger.info("modify counter {}", after.getId());
    }

    @Override
    @Transactional
    public void delete(CounterDeleteParam param) {
        CounterDO domain = counterMapper.getById(param.getId());
        if (domain == null) {
            logger.error("counter {} not exist", param.getId());
            throw new BizException(BizResultEnum.COUNTER_NOT_EXIST, param.getId());
        }

        //删除窗口前先删除关联的消息和会话
        messageService.deleteByCounter(param.getId());
        sessionService.deleteByCounter(param.getId());

        if (counterMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete counter {} failed", param.getId());
            throw new BizException(BizResultEnum.COUNTER_DELETE_ERROR);
        }

        logService.logCounterDelete(param.getId());
        logger.info("delete counter {}", param.getId());
    }

    @Override
    public BaseListResult<CounterQuerySimpleResult> list() {
        Long count = counterMapper.countAll();
        if (count == 0) {
            logger.warn("counter list result is empty");
            return new BaseListResult<>();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("counter list contain {} result(s) and divide to {} page(s)", count, pages);

        List<CounterQuerySimpleResult> all = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            List<CounterDO> domains = counterMapper.listAll((long) (i * rows), rows);
            List<CounterQuerySimpleResult> results = domains.stream()
                    .map(domain -> POJOConvertUtil.convert(domain, CounterQuerySimpleResult.class))
                    .collect(Collectors.toList());
            all.addAll(results);
        }
        logger.info("list {} counter(s)", all.size());

        BaseListResult<CounterQuerySimpleResult> result = new BaseListResult<>();
        result.setTotal(all.size());
        result.setList(all);
        return result;
    }

    @Override
    public BasePagingResult<CounterQueryResult> query(CounterQueryParam param) {
        long count = counterMapper.countByParam(param);
        if (count == 0) {
            logger.warn("counter query result is empty");
            return new BasePagingResult<>();
        }

        List<CounterDO> domains = counterMapper.listByParam(param);
        List<CounterQueryResult> results = domains.stream()
                .map(domain -> POJOConvertUtil.convert(domain, CounterQueryResult.class))
                .collect(Collectors.toList());

        appendSessionQueryResult(results);
        logger.info("query {} of {} counter(s)", results.size(), count);

        BasePagingResult<CounterQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    public CounterQuerySimpleResult queryBind(CounterQueryBindParam param) {
        CounterDO domain = counterMapper.getByMacAndIp(param.getMac(), param.getIp());
        if (domain == null) {
            logger.error("mac {} and ip {} unbind to counter", param.getMac(), param.getIp());
            throw new BizException(BizResultEnum.COUNTER_UNBIND, param.getMac(), param.getIp());
        }
        return POJOConvertUtil.convert(domain, CounterQuerySimpleResult.class);
    }

    @Override
    public Map<Long, CounterQueryResult> queryBatch(List<Long> counterIds) {
        if (CollectionUtils.isEmpty(counterIds)) {
            logger.warn("empty counter id list in query param");
            return Collections.emptyMap();
        }

        List<CounterDO> domains = counterMapper.listById(counterIds);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("counter batch query result is empty");
            return Collections.emptyMap();
        }

        logger.info("batch query {} counter(s)", domains.size());
        return domains.stream()
                .collect(Collectors.toMap(CounterDO::getId,
                        domain -> POJOConvertUtil.convert(domain, CounterQueryResult.class)));
    }

    /**
     * 检查编号是否重复
     *
     * @param number  编号
     * @param exclude 排除ID
     */
    private void checkNumberDuplicate(String number, Long exclude) {
        CounterDO domain = counterMapper.getByNumber(number);
        if (domain != null && !Objects.equals(domain.getId(), exclude)) {
            logger.error("counter number {} duplicate", number);
            throw new BizException(BizResultEnum.COUNTER_NUMBER_DUPLICATE, number);
        }
    }

    /**
     * 检查MAC地址和IP地址是否重复
     *
     * @param mac     MAC地址
     * @param ip      IP地址
     * @param exclude 排除ID
     */
    private void checkMacAndIpDuplicate(String mac, String ip, Long exclude) {
        CounterDO domain = counterMapper.getByMacAndIp(mac, ip);
        if (domain != null && !Objects.equals(domain.getId(), exclude)) {
            logger.error("counter mac {} or ip {} duplicate", mac, ip);
            throw new BizException(BizResultEnum.COUNTER_ALREADY_BIND, mac, ip);
        }
    }

    /**
     * 检查编号和名称是否被修改
     *
     * @param before 修改前的窗口信息
     * @param after  修改后的窗口信息
     */
    private void postponeWhenNumberOrNameModify(CounterDO before, CounterDO after) {
        boolean onlineFlag = sessionService.queryOnline(before.getId()).isPresent();
        boolean modifyFlag = !Objects.equals(before.getNumber(), after.getNumber())
                || !Objects.equals(before.getName(), after.getName());
        if (onlineFlag && modifyFlag) {
            messageService.sendUpdateCounterInfo(before.getId(), after.getNumber(), after.getName());
            logger.info("counter {} with online session get number or name modify", before.getId());
        }
    }

    /**
     * MAC地址和IP地址被修改后处理
     *
     * @param before 修改前的窗口信息
     * @param after  修改后的窗口信息
     */
    private void postponeWhenMacOrIpModify(CounterDO before, CounterDO after) {
        boolean onlineFlag = sessionService.queryOnline(before.getId()).isPresent();
        boolean modifyFlag = !Objects.equals(before.getMac(), after.getMac())
                || !Objects.equals(before.getIp(), after.getIp());
        if (onlineFlag && modifyFlag) {
            logger.warn("counter {} with online session get mac or ip modify", before.getId());
            throw new BizException(BizResultEnum.COUNTER_MODIFY_ONLINE);
        }
    }

    /**
     * 向窗口查询结果中追加会话查询结果
     *
     * @param results 窗口查询结果
     */
    private void appendSessionQueryResult(List<CounterQueryResult> results) {
        List<Long> counterIds = results.stream()
                .map(CounterQueryResult::getId)
                .collect(Collectors.toList());

        Map<Long, List<SessionQueryResult>> sessions = sessionService.queryBatchAndGroup(counterIds);
        results.forEach(result -> result.setSessions(sessions.get(result.getId())));
    }
}
