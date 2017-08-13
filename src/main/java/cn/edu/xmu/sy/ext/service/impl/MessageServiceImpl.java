/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.constant.RegExpConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.HttpUtils;
import cn.com.lx1992.lib.util.JsonUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.constant.FingerprintConstant;
import cn.edu.xmu.sy.ext.constant.MessageResourceConstant;
import cn.edu.xmu.sy.ext.domain.MessageDO;
import cn.edu.xmu.sy.ext.dto.FingerprintIdentifyCallbackDTO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.job.MessageResendJob;
import cn.edu.xmu.sy.ext.mapper.MessageMapper;
import cn.edu.xmu.sy.ext.message.AckMessage;
import cn.edu.xmu.sy.ext.message.BaseMessage;
import cn.edu.xmu.sy.ext.message.BaseReceiveMessage;
import cn.edu.xmu.sy.ext.message.BaseSendMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollFailureMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollReplyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollSuccessMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyFailureMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyReplyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifySuccessMessage;
import cn.edu.xmu.sy.ext.message.GeneralBusinessFailureMessage;
import cn.edu.xmu.sy.ext.message.GeneralBusinessMessage;
import cn.edu.xmu.sy.ext.message.GeneralBusinessSuccessMessage;
import cn.edu.xmu.sy.ext.message.ServiceCancelMessage;
import cn.edu.xmu.sy.ext.message.ServicePauseMessage;
import cn.edu.xmu.sy.ext.message.ServiceResumeMessage;
import cn.edu.xmu.sy.ext.message.UpdateCompanyInfoMessage;
import cn.edu.xmu.sy.ext.message.UpdateCounterInfoMessage;
import cn.edu.xmu.sy.ext.message.UpdateUserInfoMessage;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.FingerprintFingerEnum;
import cn.edu.xmu.sy.ext.meta.MessageDirectionEnum;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceCancelParam;
import cn.edu.xmu.sy.ext.param.MessageSendServicePauseParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceResumeParam;
import cn.edu.xmu.sy.ext.param.MessageSendUpdateUserInfoParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.MessageListTypeResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;
import cn.edu.xmu.sy.ext.result.MessageReplyQueryResult;
import cn.edu.xmu.sy.ext.result.ResourceQuerySimpleResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.result.SessionQuerySimpleResult;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.TtsService;
import cn.edu.xmu.sy.ext.service.UserService;
import cn.edu.xmu.sy.ext.service.WebSocketService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luoxin
 * @version 2017-4-28
 */
@CatTransaction
@Service
public class MessageServiceImpl implements MessageService {
    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FingerprintService fingerprintService;
    @Autowired
    private CounterService counterService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LogService logService;
    @Autowired
    private TtsService ttsService;
    @Autowired
    private MessageResendJob messageResendJob;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public BaseListResult<MessageListTypeResult> listType() {
        List<MessageListTypeResult> results = Stream.of(MessageTypeEnum.values())
                .filter(value -> value != MessageTypeEnum.UNKNOWN)
                .map(value -> {
                    MessageListTypeResult result = new MessageListTypeResult();
                    result.setType(value.getType());
                    result.setDescription(value.getDescription());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("list {} message type(s)", results.size());

        BaseListResult<MessageListTypeResult> result = new BaseListResult<>();
        result.setTotal(results.size());
        result.setList(results);
        return result;
    }

    @Override
    public BasePagingResult<MessageQueryResult> query(MessageQueryParam param) {
        Long count = messageMapper.countByParam(param);
        if (count == 0) {
            logger.warn("message query result is empty");
            return new BasePagingResult<>();
        }

        List<MessageDO> domains = messageMapper.listByParam(param);
        List<MessageQueryResult> results = domains.stream()
                .map(domain -> {
                    MessageQueryResult result = POJOConvertUtil.convert(domain, MessageQueryResult.class);
                    result.setType(MessageTypeEnum.getDescriptionByType(result.getType()));
                    return result;
                })
                .collect(Collectors.toList());

        appendCounterQueryResult(results);
        appendSessionQueryResult(results);
        appendReplyQueryResult(results);
        logger.info("query {} of {} message(s)", domains.size(), count);

        BasePagingResult<MessageQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    /**
     * 向消息查询结果中追加窗口查询结果
     *
     * @param results 消息查询结果
     */
    private void appendCounterQueryResult(List<MessageQueryResult> results) {
        List<Long> counterIds = results.stream()
                .map(MessageQueryResult::getCounterId)
                .collect(Collectors.toList());

        Map<Long, CounterQueryResult> counters = counterService.queryBatch(counterIds);
        results.forEach(result -> result.setCounter(counters.get(result.getCounterId())));
    }

    /**
     * 向消息查询结果中追加会话查询结果
     *
     * @param results 消息查询结果
     */
    private void appendSessionQueryResult(List<MessageQueryResult> results) {
        List<Long> sessionIds = results.stream()
                .map(MessageQueryResult::getSessionId)
                .collect(Collectors.toList());

        Map<Long, SessionQueryResult> sessions = sessionService.queryBatch(sessionIds);
        results.forEach(result -> result.setSession(sessions.get(result.getSessionId())));
    }

    /**
     * 向消息查询结果中追加回复查询结果
     *
     * @param results 消息查询结果
     */
    private void appendReplyQueryResult(List<MessageQueryResult> results) {
        List<Long> uids = results.stream()
                .map(MessageQueryResult::getUid)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(uids)) {
            logger.warn("empty uid list in query param");
            return;
        }

        List<MessageDO> domains = messageMapper.listByUid(uids, MessageDirectionEnum.RECEIVE.getDirection());
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("message list by uid result is empty");
            return;
        }

        Map<Long, MessageReplyQueryResult> replies = domains.stream()
                .map(reply -> POJOConvertUtil.convert(reply, MessageReplyQueryResult.class))
                .collect(Collectors.toMap(MessageReplyQueryResult::getId, reply -> reply));
        results.forEach(result -> result.setReply(replies.get(result.getSessionId())));
    }

    @Override
    @Transactional
    public void deleteByCounter(Long counterId) {
        List<MessageDO> domains = messageMapper.getByCounterId(counterId);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("no message for counter {}", counterId);
            return;
        }

        if (messageMapper.removeByCounterId(counterId) != domains.size()) {
            logger.error("delete message for counter {} failed", counterId);
            throw new BizException(BizResultEnum.MESSAGE_DELETE_ERROR);
        }

        logService.logMessageDeleteByCounter(counterId);
        logger.info("delete message for counter {}", counterId);
    }

    //发送消息处理逻辑
    //-----------------------------------------------------------------------
    @Override
    @Transactional
    public void sendServicePause(MessageSendServicePauseParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        ServicePauseMessage message = new ServicePauseMessage();
        message.getResources().add(
                buildResource(null, MessageResourceConstant.VOICE_SERVICE_PAUSE));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.SERVICE_PAUSE, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendServiceResume(MessageSendServiceResumeParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        ServiceResumeMessage message = new ServiceResumeMessage();
        message.getResources().add(
                buildResource(null, MessageResourceConstant.VOICE_SERVICE_RESUME));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.SERVICE_RESUME, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendServiceCancel(MessageSendServiceCancelParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        ServiceCancelMessage message = new ServiceCancelMessage();
        message.getResources().add(
                buildResource(null, MessageResourceConstant.VOICE_SERVICE_CANCEL));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.SERVICE_CANCEL, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendGeneralBusiness(MessageSendGeneralBusinessParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        GeneralBusinessMessage message = new GeneralBusinessMessage();
        message.setTimeout(param.getTimeout());

        //index=0 等待结果
        message.getExtras().add(hasExtra ? param.getExtra() : MessageResourceConstant.EXTRA_GENERAL_BUSINESS);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_GENERAL_BUSINESS,
                        hasExtra ? param.getExtra() : MessageResourceConstant.VOICE_GENERAL_BUSINESS));
        //index=1 超时
        message.getExtras().add(MessageResourceConstant.EXTRA_GENERAL_BUSINESS_TIMEOUT);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_GENERAL_BUSINESS_TIMEOUT,
                        MessageResourceConstant.VOICE_GENERAL_BUSINESS_TIMEOUT));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.GENERAL_BUSINESS, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendGeneralBusinessSuccess(MessageSendGeneralBusinessSuccessParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        GeneralBusinessSuccessMessage message = new GeneralBusinessSuccessMessage();
        message.setExtra(hasExtra ? param.getExtra() : MessageResourceConstant.EXTRA_GENERAL_BUSINESS_SUCCESS);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_GENERAL_BUSINESS_SUCCESS,
                        hasExtra ? param.getExtra() : MessageResourceConstant.VOICE_GENERAL_BUSINESS_SUCCESS));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.GENERAL_BUSINESS_SUCCESS, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendGeneralBusinessFailure(MessageSendGeneralBusinessFailureParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        GeneralBusinessFailureMessage message = new GeneralBusinessFailureMessage();
        message.setExtra(hasExtra ? param.getExtra() : MessageResourceConstant.EXTRA_GENERAL_BUSINESS_FAILURE);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_GENERAL_BUSINESS_FAILURE,
                        hasExtra ? param.getExtra() : MessageResourceConstant.VOICE_GENERAL_BUSINESS_FAILURE));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.GENERAL_BUSINESS_FAILURE, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendFingerprintEnroll(MessageSendFingerprintEnrollParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        String timeout = settingService.getValueByKeyOrDefault(SettingEnum.FINGERPRINT_ENROLL_TIMEOUT);
        String times = settingService.getValueByKeyOrDefault(SettingEnum.FINGERPRINT_ENROLL_TIMES);
        //此finger变量为手指名称
        String finger = FingerprintFingerEnum.getByFinger(param.getFinger()).getDescription();

        FingerprintEnrollMessage message = new FingerprintEnrollMessage();
        message.setUser(param.getUser());
        message.setTimes(Integer.valueOf(times));
        message.setFinger(param.getFinger());
        message.setTimeout(Integer.valueOf(timeout));

        //index=0 等待结果
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_WAIT);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_WAIT, null));
        //index=1 出错
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_ERROR);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_ERROR,
                        MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_ERROR));
        //index=2 超时
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_TIMEOUT);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_TIMEOUT,
                        MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_TIMEOUT));
        //index=3~6 等待操作(视指纹登记采集次数设置)
        switch (times) {
            case "1":
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_1_1);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_1_1,
                                MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_1_1, finger)));
                break;
            case "2":
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_1_2);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_1_2,
                                MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_1_2, finger)));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_2_2);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_2_2,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_2_2));
                break;
            case "3":
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_1_3);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_1_3,
                                MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_1_3, finger)));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_2_3);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_2_3,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_2_3));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_3_3);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_3_3,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_3_3));
                break;
            case "4":
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_1_4);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_1_4,
                                MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_1_4, finger)));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_2_4);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_2_4,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_2_4));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_3_4);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_3_4,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_3_4));
                message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_4_4);
                message.getResources().add(
                        buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_4_4,
                                MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_4_4));
                break;
        }

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_ENROLL, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    private void sendFingerprintEnrollSuccess(Long target, String finger) {
        SessionQuerySimpleResult session = getTargetSession(target);

        FingerprintEnrollSuccessMessage message = new FingerprintEnrollSuccessMessage();
        message.setExtra(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_SUCCESS);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_SUCCESS,
                        MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_SUCCESS, finger)));

        String body = JsonUtil.toJson(message);
        Long id = createSend(target, session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_ENROLL_SUCCESS, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    private void sendFingerprintEnrollFailure(Long target, String finger) {
        SessionQuerySimpleResult session = getTargetSession(target);

        FingerprintEnrollFailureMessage message = new FingerprintEnrollFailureMessage();
        message.setExtra(MessageResourceConstant.EXTRA_FINGERPRINT_ENROLL_FAILURE);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_ENROLL_FAILURE,
                        MessageFormat.format(MessageResourceConstant.VOICE_FINGERPRINT_ENROLL_FAILURE, finger)));

        String body = JsonUtil.toJson(message);
        Long id = createSend(target, session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_ENROLL_FAILURE, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendFingerprintIdentify(MessageSendFingerprintIdentifyParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        String timeout = settingService.getValueByKeyOrDefault(SettingEnum.FINGERPRINT_IDENTIFY_TIMEOUT);

        FingerprintIdentifyMessage message = new FingerprintIdentifyMessage();
        message.setTimeout(Integer.valueOf(timeout));
        //index=0 等待结果
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY_WAIT);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY_WAIT, null));
        //index=1 出错
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY_ERROR);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY_ERROR,
                        MessageResourceConstant.VOICE_FINGERPRINT_IDENTIFY_ERROR));
        //index=2 超时
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY_TIMEOUT);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY_TIMEOUT,
                        MessageResourceConstant.VOICE_FINGERPRINT_IDENTIFY_TIMEOUT));
        //index=3 等待操作
        message.getExtras().add(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY,
                        MessageResourceConstant.VOICE_FINGERPRINT_IDENTIFY));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_IDENTIFY, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    private void sendFingerprintIdentifySuccess(Long target) {
        SessionQuerySimpleResult session = getTargetSession(target);

        FingerprintIdentifySuccessMessage message = new FingerprintIdentifySuccessMessage();
        message.setExtra(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY_SUCCESS);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY_SUCCESS, null));

        String body = JsonUtil.toJson(message);
        Long id = createSend(target, session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_IDENTIFY_SUCCESS, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    private void sendFingerprintIdentifyFailure(Long target) {
        SessionQuerySimpleResult session = getTargetSession(target);

        FingerprintIdentifyFailureMessage message = new FingerprintIdentifyFailureMessage();
        message.setExtra(MessageResourceConstant.EXTRA_FINGERPRINT_IDENTIFY_FAILURE);
        message.getResources().add(
                buildResource(MessageResourceConstant.IMAGE_FINGERPRINT_IDENTIFY_FAILURE,
                        MessageResourceConstant.VOICE_FINGERPRINT_IDENTIFY_FAILURE));

        String body = JsonUtil.toJson(message);
        Long id = createSend(target, session.getId(), message.getUid(),
                MessageTypeEnum.FINGERPRINT_IDENTIFY_FAILURE, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendUpdateCompanyInfo(String logo, String name) {
        List<SessionQuerySimpleResult> sessions = sessionService.queryAllOnline();

        //向所有在线会话同时发送
        sessions.forEach(session -> {
            UpdateCompanyInfoMessage message = new UpdateCompanyInfoMessage();
            message.setName(name);
            message.getResources().add(buildResource(logo, null));

            String body = JsonUtil.toJson(message);
            Long id = createSend(session.getCounterId(), session.getId(), message.getUid(),
                    MessageTypeEnum.UPDATE_COMPANY_INFO, body);
            sendAndScheduleRetry(id, session.getToken(), body);
        });
    }

    @Override
    @Transactional
    public void sendUpdateCounterInfo(Long target, String number, String name) {
        SessionQuerySimpleResult session = getTargetSession(target);

        UpdateCounterInfoMessage message = new UpdateCounterInfoMessage();
        message.setNumber(number);
        message.setName(name);

        String body = JsonUtil.toJson(message);
        Long id = createSend(target, session.getId(), message.getUid(),
                MessageTypeEnum.UPDATE_COUNTER_INFO, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void sendUpdateUserInfo(MessageSendUpdateUserInfoParam param) {
        SessionQuerySimpleResult session = getTargetSession(param.getTarget());

        UpdateUserInfoMessage message = new UpdateUserInfoMessage();
        message.setNumber(param.getNumber());
        message.setName(param.getName());
        message.getResources().add(
                buildResource(param.getPhoto(),
                        MessageFormat.format(MessageResourceConstant.VOICE_UPDATE_USER_INFO, param.getName())));

        String body = JsonUtil.toJson(message);
        Long id = createSend(param.getTarget(), session.getId(), message.getUid(),
                MessageTypeEnum.UPDATE_USER_INFO, body);
        sendAndScheduleRetry(id, session.getToken(), body);
    }

    @Override
    @Transactional
    public void resend(Long uid) {
        List<MessageDO> domains = messageMapper.getByUid(uid, MessageDirectionEnum.SEND.getDirection());
        if (CollectionUtils.isEmpty(domains)) {
            logger.error("message with uid {} not exist", uid);
            throw new BizException(BizResultEnum.MESSAGE_UID_NOT_EXIST);
        }

        MessageDO before = domains.get(0);
        //获取原Token
        String token = sessionService.queryIfOnline(before.getSessionId()).getToken();
        //更新重试次数
        MessageDO after = new MessageDO();
        after.setId(before.getId());
        after.setRetry(before.getRetry() + 1);
        if (messageMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("update message {} failed", after.getId());
            throw new BizException(BizResultEnum.MESSAGE_UPDATE_ERROR);
        }
        //再次发送
        sendAndScheduleRetry(before.getId(), token, before.getBody());
    }

    //发送消息辅助逻辑
    //-----------------------------------------------------------------------

    /**
     * 查询消息发送目标当前在线会话
     *
     * @param target 消息发送目标
     * @return 查询结果
     */
    private SessionQuerySimpleResult getTargetSession(Long target) {
        return sessionService.queryOnline(target)
                .orElseThrow(() -> {
                    logger.error("target counter {} has no session online", target);
                    return new BizException(BizResultEnum.MESSAGE_TARGET_NOT_ONLINE);
                });
    }

    /**
     * 构造发送的消息中的资源
     *
     * @param image  图像
     * @param voices 声音
     * @return 资源
     */
    private BaseSendMessage.Resource buildResource(String image, String voices) {
        BaseSendMessage.Resource resource = new BaseSendMessage.Resource();
        if (!StringUtils.isEmpty(image)) {
            if (image.matches(RegExpConstant.HTTP_URL_PREFIX)) {
                //绝对地址说明资源来自外部系统
                resource.setImage(image);
            } else {
                Optional<ResourceQuerySimpleResult> result =
                        resourceService.queryByTag(ResourceTypeEnum.IMAGE.getType(), image);
                resource.setImage(result.map(ResourceQuerySimpleResult::getUrl).orElse(null));
            }
        }
        if (!StringUtils.isEmpty(voices)) {
            //将文本按标点符号分割，分割后的子句可复用，减少资源数量和体积
            for (String voice : voices.split(RegExpConstant.PUNCTUATION)) {
                Optional<ResourceQuerySimpleResult> result =
                        resourceService.queryByTag(ResourceTypeEnum.VOICE.getType(), voice);
                if (result.isPresent()) {
                    resource.getVoices().add(result.get().getUrl());
                } else {
                    String filename = ttsService.ttsSync(voice, false);
                    //资源URL格式：类型+“/”+文件名
                    String url = ResourceTypeEnum.VOICE.getType() + CommonConstant.SLASH_STRING + filename;
                    resource.getVoices().add(url);
                }
            }
        }
        return resource;
    }

    /**
     * 创建发送的消息
     *
     * @param counterId 窗口ID
     * @param sessionId 会话ID
     * @param uid       UID
     * @param type      消息类型
     * @param body      消息体
     * @return 消息ID
     */
    private Long createSend(Long counterId, Long sessionId, Long uid, MessageTypeEnum type, String body) {
        MessageDO domain = new MessageDO();
        domain.setCounterId(counterId);
        domain.setSessionId(sessionId);
        domain.setUid(uid);
        domain.setDirection(MessageDirectionEnum.SEND.getDirection());
        domain.setType(type.getType());
        domain.setBody(body);
        domain.setRetry(1);
        domain.setSendTime(DateTimeUtil.getNow());

        if (messageMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create send message {} failed", domain.getId());
            throw new BizException(BizResultEnum.MESSAGE_CREATE_ERROR);
        }
        logService.logMessageSend(domain.getId(), counterId, sessionId, uid, type, body.length());
        logger.info("create send message {}", domain.getId());

        return domain.getId();
    }

    /**
     * 发送消息并计划重试
     *
     * @param id    消息ID
     * @param token Token
     * @param body  消息体
     */
    private void sendAndScheduleRetry(Long id, String token, String body) {
        webSocketService.sendMessage(token, body);
        messageResendJob.schedule(id);
    }

    //接收消息处理逻辑
    //-----------------------------------------------------------------------
    @Override
    @Transactional
    public void receive(String token, String body, LocalDateTime timestamp) {
        //先转换成BaseMessage判断类型
        BaseMessage message = JsonUtil.toObject(body, BaseMessage.class);
        MessageTypeEnum type = MessageTypeEnum.getByType(message.getType());

        if (MessageTypeEnum.ACK.equals(type)) {
            receiveAck(body);
        } else {
            sendAck(token, message.getUid());
            receiveOther(message.getUid(), type, body, timestamp);
        }
    }

    /**
     * 接收到ACK
     *
     * @param body 消息体
     */
    private void receiveAck(String body) {
        //根据UID查询关联消息
        MessageDO associated = getAssociated(JsonUtil.toObject(body, AckMessage.class).getUid());

        MessageDO domain = new MessageDO();
        domain.setId(associated.getId());
        domain.setAckTime(DateTimeUtil.getNow());
        if (messageMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("update message {} failed", domain.getId());
            throw new BizException(BizResultEnum.MESSAGE_UPDATE_ERROR);
        }

        //消息已确认，取消重发
        messageResendJob.cancel(associated.getId());
    }

    /**
     * 接收到其他类型消息
     *
     * @param uid       UID
     * @param type      类型
     * @param body      消息体
     * @param timestamp 时间戳
     */
    private void receiveOther(Long uid, MessageTypeEnum type, String body, LocalDateTime timestamp) {
        boolean flag = createReceive(uid, type, body, timestamp);
        //根据flag & type进一步处理
        if (flag) {
            if (MessageTypeEnum.FINGERPRINT_ENROLL_REPLY.equals(type)) {
                BaseReceiveMessage message = JsonUtil.toObject(body, FingerprintEnrollReplyMessage.class);
                receiveFingerprintEnrollReply((FingerprintEnrollReplyMessage) message);
            } else if (MessageTypeEnum.FINGERPRINT_IDENTIFY_REPLY.equals(type)) {
                BaseReceiveMessage message = JsonUtil.toObject(body, FingerprintIdentifyReplyMessage.class);
                receiveFingerprintIdentifyReply((FingerprintIdentifyReplyMessage) message);
            } else {
                logger.error("receive message with uid {} has unknown type {}", uid);
                throw new BizException(BizResultEnum.MESSAGE_RECEIVE_UNKNOWN_TYPE);
            }
        }
    }

    /**
     * 发送ACK
     * 用于接收到指纹登记回复/指纹辨识回复
     *
     * @param token Token
     * @param uid   UID
     */
    private void sendAck(String token, Long uid) {
        AckMessage message = new AckMessage();
        message.setUid(uid);

        String body = JsonUtil.toJson(message);
        webSocketService.sendMessage(token, body);
    }

    /**
     * 接收指纹登记回复
     *
     * @param message 消息
     */
    private void receiveFingerprintEnrollReply(FingerprintEnrollReplyMessage message) {
        //仅处理正确提取到模板的情形
        if (FingerprintConstant.MESSAGE_REPLY_STATUS_EXTRACT.equals(message.getStatus())) {
            //重新解析原来发送的消息
            MessageDO associated = getAssociated(message.getUid());
            FingerprintEnrollMessage sent =
                    JsonUtil.toObject(associated.getBody(), FingerprintEnrollMessage.class);

            //进行指纹登记，如果抛出异常回复登记失败
            try {
                fingerprintService.enroll(sent.getUser(), sent.getFinger(), message.getTemplate());
            } catch (BizException e) {
                sendFingerprintEnrollFailure(associated.getCounterId(),
                        FingerprintFingerEnum.getByFinger(sent.getFinger()).getDescription());
                return;
            }

            sendFingerprintEnrollSuccess(associated.getCounterId(),
                    FingerprintFingerEnum.getByFinger(sent.getFinger()).getDescription());
        }
    }

    /**
     * 接收到指纹辨识回复
     *
     * @param message 消息
     */
    private void receiveFingerprintIdentifyReply(FingerprintIdentifyReplyMessage message) {
        if (FingerprintConstant.MESSAGE_REPLY_STATUS_EXTRACT.equals(message.getStatus())) {
            //重新解析原来发送的消息
            MessageDO associated = getAssociated(message.getUid());

            //辨识指纹对应的用户ID，获取该用户详细信息，并调用第三方回调
            try {
                Long userId = fingerprintService.identify(message.getTemplate());
                UserQueryResult result = userService.queryById(userId);
                invokeFingerprintIdentifyCallback(result.getNumber(), result.getName(), result.getPhoto());
            } catch (BizException e) {
                sendFingerprintIdentifyFailure(associated.getCounterId());
                return;
            }

            sendFingerprintIdentifySuccess(associated.getCounterId());
        }
    }

    //接收消息辅助逻辑
    //-----------------------------------------------------------------------

    /**
     * 创建接收的消息
     *
     * @param uid       消息ID
     * @param type      消息类型
     * @param body      消息体
     * @param timestamp 时间戳
     * @return true-继续处理，false-停止处理
     */
    private boolean createReceive(Long uid, MessageTypeEnum type, String body, LocalDateTime timestamp) {
        MessageDO idempotent = getIdempotent(uid);
        if (idempotent == null) {
            MessageDO associated = getAssociated(uid);

            MessageDO domain = new MessageDO();
            domain.setCounterId(associated.getCounterId());
            domain.setSessionId(associated.getSessionId());
            domain.setUid(uid);
            domain.setDirection(MessageDirectionEnum.RECEIVE.getDirection());
            domain.setType(type.getType());
            domain.setBody(body);
            domain.setRetry(1);
            domain.setReceiveTime(timestamp);

            if (messageMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
                logger.error("create receive message {} failed", domain.getId());
                throw new BizException(BizResultEnum.MESSAGE_CREATE_ERROR);
            }
            logService.logMessageReceive(domain.getId(), uid, type.getType(), body.length());
            logger.info("create receive message {}", domain.getId());
        } else {
            MessageDO domain = new MessageDO();
            domain.setId(idempotent.getId());
            domain.setRetry(idempotent.getRetry() + 1);

            if (messageMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
                logger.error("update message {} failed", domain.getId());
                throw new BizException(BizResultEnum.MESSAGE_UPDATE_ERROR);
            }
            logger.info("update message {}", domain.getId());
        }

        return idempotent == null;
    }

    /**
     * 获取与接收到的消息关联的发送的消息
     *
     * @param uid UID
     * @return 关联的发送的消息
     */
    private MessageDO getAssociated(Long uid) {
        List<MessageDO> domains = messageMapper.getByUid(uid, MessageDirectionEnum.SEND.getDirection());
        if (CollectionUtils.isEmpty(domains)) {
            logger.error("message associate to uid {} not exist", uid);
            throw new BizException(BizResultEnum.MESSAGE_UID_NOT_EXIST, uid);
        }
        return domains.get(0);
    }

    /**
     * 获取幂等的接收的消息
     * 若幂等返回null，否则返回已有消息
     *
     * @param uid UID
     * @return 已有的消息
     */
    private MessageDO getIdempotent(Long uid) {
        List<MessageDO> domains = messageMapper.getByUid(uid, MessageDirectionEnum.RECEIVE.getDirection());
        return !CollectionUtils.isEmpty(domains) ? domains.get(0) : null;
    }

    /**
     * 向第三方系统反馈指纹辨识结果
     *
     * @param number 用户编号
     * @param name   用户姓名
     * @param photo  用户照片
     */
    private void invokeFingerprintIdentifyCallback(String number, String name, String photo) {
        String callback = settingService.getValueByKeyOrDefault(SettingEnum.FINGERPRINT_IDENTIFY_CALLBACK);
        if (StringUtils.isEmpty(callback)) {
            logger.warn("fingerprint identify callback is empty");
            return;
        }

        FingerprintIdentifyCallbackDTO result = new FingerprintIdentifyCallbackDTO();
        result.setName(number);
        result.setName(name);
        result.setPhoto(photo);

        //调用设置的回调API
        try {
            HttpUtils.execute(callback, JsonUtil.toJson(result));
        } catch (IOException e) {
            logger.error("invoke fingerprint identify callback {} failed", callback);
            throw new BizException(BizResultEnum.MESSAGE_INVOKE_CALLBACK_ERROR);
        }
    }
}
