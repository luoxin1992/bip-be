/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.dto.DiffFieldDTO;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.domain.LogDO;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.LogMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.LogEnum;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import cn.edu.xmu.sy.ext.result.LogCategoryListResult;
import cn.edu.xmu.sy.ext.result.LogQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-25
 */
@Service
public class LogServiceImpl implements LogService {
    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private LogMapper logMapper;

    @Override
    public BasePagingResult<LogQueryResult> query(LogQueryParam param) {
        long count = logMapper.countByParam(param);
        if (count == 0) {
            logger.warn("log query result is empty");
            throw new BizException(BizResultEnum.LOG_EMPTY_QUERY_RESULT);
        }

        List<LogDO> domains = logMapper.listByParam(param);
        List<LogQueryResult> results = domains.stream()
                .map((domain) -> {
                    LogQueryResult result = POJOConvertUtil.convert(domain, LogQueryResult.class);
                    result.setTimestamp(domain.getGmtCreate());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("query {}/{} log(s)", results.size(), count);

        BasePagingResult<LogQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    public LogCategoryListResult listCategory() {
        List<String> results = Arrays.stream(LogEnum.values())
                .map(LogEnum::getCategory)
                .collect(Collectors.toList());

        logger.info("list {} category(s)", results.size());
        LogCategoryListResult result = new LogCategoryListResult();
        result.setCategories(results);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logUserCreate(UserDO user) {
        String category = LogEnum.USER_CREATE.getCategory();
        String content = MessageFormat.format(LogEnum.USER_CREATE.getContent(), user.getId(), user.getNumber(),
                user.getName());
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logUserModify(UserDO before, UserDO after) {
        String category = LogEnum.USER_MODIFY.getCategory();
        String content = MessageFormat.format(LogEnum.USER_MODIFY.getContent(), before.getId(),
                buildModifyLogContent(POJOCompareUtil.compare(UserDO.class, before, after)));
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logUserDelete(Long id) {
        String category = LogEnum.USER_DELETE.getCategory();
        String content = MessageFormat.format(LogEnum.USER_DELETE.getContent(), id);
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFingerprintCreate(FingerprintDO fingerprint) {
        String category = LogEnum.FINGERPRINT_CREATE.getCategory();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_CREATE.getContent(), fingerprint.getUserId(),
                fingerprint.getId());
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFingerprintDeleteById(Long id) {
        String category = LogEnum.FINGERPRINT_DELETE.getCategory();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_DELETE.getContent(), id);
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFingerprintDeleteByUserId(Long userId) {
        String category = LogEnum.FINGERPRINT_DELETE_BY_USER.getCategory();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_DELETE_BY_USER.getContent(), userId);
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCounterCreate(CounterDO counter) {
        String category = LogEnum.COUNTER_CREATE.getCategory();
        String content = MessageFormat.format(LogEnum.COUNTER_CREATE.getContent(), counter.getId(), counter.getNumber(),
                counter.getName(), counter.getMac(), counter.getIp());
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCounterModify(CounterDO before, CounterDO after) {
        String category = LogEnum.COUNTER_MODIFY.getCategory();
        String content = MessageFormat.format(LogEnum.COUNTER_MODIFY.getContent(), before.getId(),
                buildModifyLogContent(POJOCompareUtil.compare(CounterDO.class, before, after)));
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logCounterDelete(Long id) {
        String category = LogEnum.COUNTER_DELETE.getCategory();
        String content = MessageFormat.format(LogEnum.COUNTER_DELETE.getContent(), id);
        createLog(category, content);
    }

    @Override
    public void logSessionOnline() {

    }

    @Override
    public void logSessionOffline() {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logSettingSave(String description, String oldValue, String newValue) {
        String category = LogEnum.SETTING_SAVE.getCategory();
        String content = MessageFormat.format(LogEnum.SETTING_SAVE.getContent(), description, oldValue, newValue);
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logResourceCreate(ResourceDO resource) {
        String category = LogEnum.RESOURCE_CREATE.getCategory();
        String content = MessageFormat.format(LogEnum.RESOURCE_CREATE.getContent(), resource.getId(),
                resource.getName(), resource.getPath());
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logResourceModify(ResourceDO before, ResourceDO after) {
        String category = LogEnum.RESOURCE_MODIFY.getCategory();
        String content = MessageFormat.format(LogEnum.RESOURCE_MODIFY.getContent(), before.getId(),
                buildModifyLogContent(POJOCompareUtil.compare(ResourceDO.class, before, after)));
        createLog(category, content);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logResourceDelete(Long id) {
        String category = LogEnum.RESOURCE_DELETE.getCategory();
        String content = MessageFormat.format(LogEnum.RESOURCE_DELETE.getContent(), id);
        createLog(category, content);
    }

    private String buildModifyLogContent(List<DiffFieldDTO> diffFields) {
        return diffFields.stream()
                .filter((field) -> field.getFromValue() != null && field.getToValue() != null)
                .map((field) -> field.getFieldName() + "由'" + field.getFromValue() + "'改为'" + field.getToValue() + "'")
                .collect(Collectors.joining(", "));
    }

    private void createLog(String category, String content) {
        LogDO domain = new LogDO();
        domain.setCategory(category);
        domain.setContent(content);
        if (logMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create log with id {} failed", domain.getId());
            throw new BizException(BizResultEnum.LOG_CREATE_ERROR);
        }
    }
}
