/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.dto.DiffFieldDTO;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.LogDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.LogMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.LogEnum;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import cn.edu.xmu.sy.ext.result.LogQueryResult;
import cn.edu.xmu.sy.ext.result.LogTypeListResult;
import cn.edu.xmu.sy.ext.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-25
 */
@CatTransaction
@Service
public class LogServiceImpl implements LogService {
    private final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogTypeListResult listType() {
        List<String> results = Arrays.stream(LogEnum.values())
                .map(LogEnum::getType)
                .collect(Collectors.toList());
        logger.info("list {} log type(s)", results.size());

        LogTypeListResult result = new LogTypeListResult();
        result.setTypes(results);
        return result;
    }

    @Override
    public BasePagingResult<LogQueryResult> query(LogQueryParam param) {
        Long count = logMapper.countByParam(param);
        if (count == 0) {
            logger.warn("log query result is empty");
            return new BasePagingResult<>();
        }

        List<LogDO> domains = logMapper.listByParam(param);
        List<LogQueryResult> results = domains.stream()
                .map(domain -> {
                    LogQueryResult result = POJOConvertUtil.convert(domain, LogQueryResult.class);
                    result.setTimestamp(domain.getGmtCreate());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("query {} of {} log(s)", results.size(), count);

        BasePagingResult<LogQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    @Transactional
    public void logUserCreate(Long userId, String number, String name) {
        String type = LogEnum.USER_CREATE.getType();
        String content = MessageFormat.format(LogEnum.USER_CREATE.getContent(), userId, number, name);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logUserModify(Long userId, List<DiffFieldDTO> diffFields) {
        String type = LogEnum.USER_MODIFY.getType();
        String content = MessageFormat.format(LogEnum.USER_MODIFY.getContent(), userId, buildModifyContent(diffFields));
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logUserDelete(Long userId) {
        String type = LogEnum.USER_DELETE.getType();
        String content = MessageFormat.format(LogEnum.USER_DELETE.getContent(), userId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logFingerprintModify(Long fingerprintId, List<DiffFieldDTO> diffFields) {
        String type = LogEnum.FINGERPRINT_MODIFY.getType();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_MODIFY.getContent(), fingerprintId, buildModifyContent(diffFields));
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logFingerprintDelete(Long fingerprintId) {
        String type = LogEnum.FINGERPRINT_DELETE.getType();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_DELETE.getContent(), fingerprintId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logFingerprintDeleteByUser(Long userId) {
        String type = LogEnum.FINGERPRINT_DELETE_BY_USER.getType();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_DELETE_BY_USER.getContent(), userId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logFingerprintEnroll(Long fingerprintId, Long userId, Integer fingerprintUid) {
        String type = LogEnum.FINGERPRINT_ENROLL.getType();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_ENROLL.getContent(), fingerprintId, userId, fingerprintUid);
        createLog(type, content);
    }

    @Override
    public void logFingerprintIdentify(Long userId, Long fingerprintId, Integer fingerprintUid) {
        String type = LogEnum.FINGERPRINT_IDENTIFY.getType();
        String content = MessageFormat.format(LogEnum.FINGERPRINT_IDENTIFY.getContent(), fingerprintId, userId, fingerprintUid);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logCounterCreate(Long counterId, String number, String name) {
        String type = LogEnum.COUNTER_CREATE.getType();
        String content = MessageFormat.format(LogEnum.COUNTER_CREATE.getContent(), counterId, number, name);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logCounterModify(Long counterId, List<DiffFieldDTO> diffFields) {
        String type = LogEnum.COUNTER_MODIFY.getType();
        String content = MessageFormat.format(LogEnum.COUNTER_MODIFY.getContent(), counterId, buildModifyContent(diffFields));
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logCounterDelete(Long counterId) {
        String type = LogEnum.COUNTER_DELETE.getType();
        String content = MessageFormat.format(LogEnum.COUNTER_DELETE.getContent(), counterId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logSessionCreate(Long sessionId, Long counterId, String token) {
        String type = LogEnum.SESSION_CREATE.getType();
        String content = MessageFormat.format(LogEnum.SESSION_CREATE.getContent(), sessionId, counterId, token);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logSessionUpdateStatus(Long sessionId, String token, String status) {
        String type = LogEnum.SESSION_STATUS_UPDATE.getType();
        String content = MessageFormat.format(LogEnum.SESSION_STATUS_UPDATE.getContent(), sessionId, token, status);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logSessionDeleteByCounter(Long counterId) {
        String type = LogEnum.SESSION_DELETE_BY_COUNTER.getType();
        String content = MessageFormat.format(LogEnum.SESSION_DELETE_BY_COUNTER.getContent(), counterId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logMessageSend(Long messageId, Long counterId, Long sessionId, Long messageUid, MessageTypeEnum messageType, Integer messageLength) {
        String type = LogEnum.MESSAGE_SEND.getType();
        String content = MessageFormat.format(LogEnum.MESSAGE_SEND.getContent(), messageId, counterId, sessionId, messageUid, messageType.getDescription(), messageLength);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logMessageReceive(Long messageId, Long messageUid, String messageType, Integer messageLength) {
        String type = LogEnum.MESSAGE_RECEIVE.getType();
        String content = MessageFormat.format(LogEnum.MESSAGE_RECEIVE.getContent(), messageId, messageUid, messageType, messageLength);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logMessageDeleteByCounter(Long counterId) {
        String type = LogEnum.MESSAGE_DELETE_BY_COUNTER.getType();
        String content = MessageFormat.format(LogEnum.MESSAGE_DELETE_BY_COUNTER.getContent(), counterId);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logSettingSave(String description, String from, String to) {
        String type = LogEnum.SETTING_SAVE.getType();
        String content = MessageFormat.format(LogEnum.SETTING_SAVE.getContent(), description, from, to);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logResourceCreate(Long resourceId, String tag, String filename) {
        String type = LogEnum.RESOURCE_CREATE.getType();
        String content = MessageFormat.format(LogEnum.RESOURCE_CREATE.getContent(), resourceId, tag, filename);
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logResourceModify(Long resourceId, List<DiffFieldDTO> diffFields) {
        String type = LogEnum.RESOURCE_MODIFY.getType();
        String content = MessageFormat.format(LogEnum.RESOURCE_MODIFY.getContent(), resourceId, buildModifyContent(diffFields));
        createLog(type, content);
    }

    @Override
    @Transactional
    public void logResourceDelete(Long resourceId) {
        String type = LogEnum.RESOURCE_DELETE.getType();
        String content = MessageFormat.format(LogEnum.RESOURCE_DELETE.getContent(), resourceId);
        createLog(type, content);
    }

    /**
     * 构造修改型日志内容
     *
     * @param diffFields 修改的字段
     * @return 内容
     */
    private String buildModifyContent(List<DiffFieldDTO> diffFields) {
        return diffFields.stream()
                //修改为null表示未修改，不记录日志
                .filter((field) -> field.getToValue() != null)
                .map((field) -> field.getFieldName() + "由'" + field.getFromValue() + "'改为'" + field.getToValue() + "'")
                .collect(Collectors.joining(", "));
    }

    /**
     * 新增日志
     *
     * @param type    类型
     * @param content 内容
     */
    private void createLog(String type, String content) {
        LogDO domain = new LogDO();
        domain.setType(type);
        domain.setContent(content);
        if (logMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create log {} failed", domain.getId());
            throw new BizException(BizResultEnum.LOG_CREATE_ERROR);
        }
    }
}
