/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.constant.BaseFieldNameConstant;
import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.constant.RegExpConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.HttpUtils;
import cn.com.lx1992.lib.util.JsonUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.config.DependencyConfigItem;
import cn.edu.xmu.sy.ext.constant.MessageResourceNameConstant;
import cn.edu.xmu.sy.ext.domain.MessageDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.MessageMapper;
import cn.edu.xmu.sy.ext.message.BaseMessage;
import cn.edu.xmu.sy.ext.message.BaseMessageResource;
import cn.edu.xmu.sy.ext.message.BusinessPauseMessage;
import cn.edu.xmu.sy.ext.message.BusinessProcessMessage;
import cn.edu.xmu.sy.ext.message.BusinessResumeMessage;
import cn.edu.xmu.sy.ext.message.CloseMessage;
import cn.edu.xmu.sy.ext.message.CounterInfoMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollReplyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintEnrollSuccessMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyFailureMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyReplyMessage;
import cn.edu.xmu.sy.ext.message.FingerprintIdentifyTimeoutMessage;
import cn.edu.xmu.sy.ext.message.UserInfoMessage;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.FingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.FingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessPauseParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessProcessParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessResumeParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageCloseParam;
import cn.edu.xmu.sy.ext.param.MessageCounterInfoParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageReplyParam;
import cn.edu.xmu.sy.ext.param.MessageSendRemoteParam;
import cn.edu.xmu.sy.ext.param.MessageSendToParam;
import cn.edu.xmu.sy.ext.param.MessageTokenRegisterRemoteParam;
import cn.edu.xmu.sy.ext.param.MessageTokenUnregisterRemoteParam;
import cn.edu.xmu.sy.ext.param.MessageUserInfoParam;
import cn.edu.xmu.sy.ext.result.MessageFingerprintIdentifyResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;
import cn.edu.xmu.sy.ext.result.MessageReplyQueryResult;
import cn.edu.xmu.sy.ext.result.MessageTypeListResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author luoxin
 * @version 2017-4-28
 */
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
    private SettingService settingService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private LogService logService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private DependencyConfigItem dependencyConfigItem;

    private Map<Long, CountDownLatch> pendingMessageLatchMap = new ConcurrentHashMap<>();

    @Override
    public MessageTypeListResult listType() {
        List<String> types = Stream.of(MessageTypeEnum.values())
                .filter(value -> value != MessageTypeEnum.UNKNOWN)
                .map(MessageTypeEnum::getDescription)
                .collect(Collectors.toList());

        MessageTypeListResult result = new MessageTypeListResult();
        result.setTypes(types);
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

        List<Long> ids = domains.stream()
                .map(MessageDO::getId)
                .collect(Collectors.toList());
        Map<Long, MessageReplyQueryResult> replies = batchQueryReplyAndMap(ids);

        List<MessageQueryResult> results = domains.stream()
                .map(domain -> {
                    MessageQueryResult result = POJOConvertUtil.convert(domain, MessageQueryResult.class);
                    result.setType(MessageTypeEnum.getDescriptionByType(domain.getType()));
                    return result;
                })
                .peek(result -> result.setReply(replies.get(result.getId())))
                .collect(Collectors.toList());
        logger.info("query {}/{} message(s)", domains.size(), count);

        BasePagingResult<MessageQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    @Transactional
    public void sendTokenRegister(String token) {
        MessageTokenRegisterRemoteParam param = new MessageTokenRegisterRemoteParam();
        param.setToken(token);
        JsonNode response = callRemote(dependencyConfigItem.getApiMsgTokenRegister(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            logger.error("register token to remote get error {}", code);
            throw new BizException(BizResultEnum.MESSAGE_REMOTE_SERVICE_ERROR, code);
        }
    }

    @Override
    @Transactional
    public void sendTokenUnregister(String token) {
        MessageTokenUnregisterRemoteParam param = new MessageTokenUnregisterRemoteParam();
        param.setToken(token);
        JsonNode response = callRemote(dependencyConfigItem.getApiMsgTokenRegister(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            logger.error("unregister token to remote get error {}", code);
            throw new BizException(BizResultEnum.MESSAGE_REMOTE_SERVICE_ERROR, code);
        }
    }

    @Override
    @Transactional
    public void sendBusinessPause(MessageBusinessPauseParam param) {
        BusinessPauseMessage message = new BusinessPauseMessage();
        //TODO 暂停服务和恢复服务有无图像资源？
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_BUSINESS_PAUSE,
                MessageResourceNameConstant.VOICE_BUSINESS_PAUSE));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.BUSINESS_PAUSE.getDescription(), body.length());
        logger.info("send business pause message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendBusinessResume(MessageBusinessResumeParam param) {
        BusinessResumeMessage message = new BusinessResumeMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_BUSINESS_RESUME,
                MessageResourceNameConstant.VOICE_BUSINESS_RESUME));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.BUSINESS_RESUME.getDescription(), body.length());
        logger.info("send business resume message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendBusinessProcess(MessageBusinessProcessParam param) {
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        BusinessProcessMessage message = new BusinessProcessMessage();
        message.setExtra(hasExtra ? param.getExtra() : null);
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_BUSINESS_PROCESS,
                hasExtra ? param.getExtra() : MessageResourceNameConstant.VOICE_BUSINESS_PROCESS));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.BUSINESS_PROCESS.getDescription(), body.length());
        logger.info("send business process message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendBusinessSuccess(MessageBusinessSuccessParam param) {
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        BusinessProcessMessage message = new BusinessProcessMessage();
        message.setExtra(hasExtra ? param.getExtra() : null);
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_BUSINESS_SUCCESS,
                hasExtra ? param.getExtra() : MessageResourceNameConstant.VOICE_BUSINESS_SUCCESS));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.BUSINESS_SUCCESS.getDescription(), body.length());
        logger.info("send business success message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendBusinessFailure(MessageBusinessFailureParam param) {
        boolean hasExtra = !StringUtils.isEmpty(param.getExtra());

        BusinessProcessMessage message = new BusinessProcessMessage();
        message.setExtra(hasExtra ? param.getExtra() : null);
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_BUSINESS_FAILURE,
                hasExtra ? param.getExtra() : MessageResourceNameConstant.VOICE_BUSINESS_FAILURE));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.BUSINESS_FAILURE.getDescription(), body.length());
        logger.info("send business failure message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendFingerprintEnroll(MessageFingerprintEnrollParam param) {
        String times = settingService.getValueByKeyOptional(SettingEnum.FINGERPRINT_ENROLL_TIMES.getKey())
                .orElse(SettingEnum.FINGERPRINT_ENROLL_TIMES.getDefaultValue());
        //设置不同的指纹采集次数，下发的资源不同
        List<BaseMessageResource> resources = new ArrayList<>();
        switch (times) {
            case "1":
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_1_1,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_1_1));
                break;
            case "2":
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_1_2,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_1_2));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_2_2,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_2_2));
                break;
            case "3":
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_1_3,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_1_3));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_2_3,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_2_3));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_3_3,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_3_3));
                break;
            case "4":
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_1_4,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_1_4));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_2_4,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_2_4));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_3_4,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_3_4));
                resources.add(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_4_4,
                        MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_4_4));
                break;
        }

        FingerprintEnrollMessage message = new FingerprintEnrollMessage();
        message.setTimes(Integer.valueOf(times));
        message.setResources(resources);

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_IDENTIFY.getDescription(), body.length());
        logger.info("send fingerprint enroll message {} successful", messageId);

        //等待回复
        //TODO worker线程被阻塞
        pendingMessageLatchMap.put(messageId, new CountDownLatch(1));
        try {
            //若后续处理步骤无异常，则再发送登记成功提示消息
            sendFingerprintEnrollPostProcess(messageId, pendingMessageLatchMap.get(messageId));
            sendFingerprintEnrollSuccessMessage(param.getSendTo());
        } catch (BizException e) {
            //处理登记失败和登记超时两种情况
            //TODO 只有重复登记的异常 登记手指过多等？
            if (BizResultEnum.FINGERPRINT_ALREADY_ENROLL.getCode().equals(e.getCode())) {
                sendFingerprintEnrollFailureMessage(param.getSendTo());
            }
            if (BizResultEnum.MESSAGE_REPLY_TIMEOUT.getCode().equals(e.getCode())) {
                sendFingerprintEnrollTimeoutMessage(param.getSendTo());

            }
            throw e;
        } finally {
            pendingMessageLatchMap.remove(messageId);
        }
    }

    @Override
    @Transactional
    public MessageFingerprintIdentifyResult sendFingerprintIdentify(MessageFingerprintIdentifyParam param) {
        FingerprintIdentifyMessage message = new FingerprintIdentifyMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_IDENTIFY,
                MessageResourceNameConstant.VOICE_FINGERPRINT_IDENTIFY));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_IDENTIFY.getDescription(), body.length());
        logger.info("send fingerprint identify message {} successful", messageId);

        //等待回复
        //TODO worker线程被阻塞
        pendingMessageLatchMap.put(messageId, new CountDownLatch(1));
        try {
            return sendFingerprintIdentifyPostProcess(messageId, pendingMessageLatchMap.get(messageId));
        } catch (BizException e) {
            //辨识失败和回复超时两种情况，需要再发送一条相应的提示消息
            if (BizResultEnum.FINGERPRINT_IDENTIFY_MISMATCH.getCode().equals(e.getCode())) {
                sendFingerprintIdentifyFailureMessage(param.getSendTo());
            }
            if (BizResultEnum.MESSAGE_REPLY_TIMEOUT.getCode().equals(e.getCode())) {
                sendFingerprintIdentifyTimeoutMessage(param.getSendTo());
            }
            throw e;
        } finally {
            pendingMessageLatchMap.remove(messageId);
        }
    }

    @Override
    @Transactional
    public void sendClose(MessageCloseParam param) {
        CloseMessage message = new CloseMessage();

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.CLOSE.getDescription(), body.length());
        logger.info("send close message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendCounterInfo(MessageCounterInfoParam param) {
        CounterInfoMessage message = new CounterInfoMessage();
        message.setNumber(param.getNumber());
        message.setName(param.getName());
        message.setResource(buildResource(null, MessageResourceNameConstant.VOICE_COUNTER_INFO));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.COUNTER_INFO.getDescription(), body.length());
        logger.info("send counter info message {} successful", messageId);
    }

    @Override
    @Transactional
    public void sendUserInfo(MessageUserInfoParam param) {
        UserInfoMessage message = new UserInfoMessage();
        message.setNumber(param.getNumber());
        message.setName(param.getName());
        message.setPhoto(param.getPhoto());
        message.setResource(buildResource(null, MessageResourceNameConstant.VOICE_USER_INFO + param.getName()));

        Long sessionId = getSendToSession(param.getSendTo());
        Long messageId = createMessage(param.getSendTo().getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, param.getSendTo().getId(), sessionId,
                MessageTypeEnum.USER_INFO.getDescription(), body.length());
        logger.info("send user info message {} successful", messageId);
    }

    @Override
    @Transactional
    public void receiveReply(MessageReplyParam param) {
        String body = param.getBody();
        //先转换成BaseMessage判断类型
        BaseMessage message = JsonUtil.toObject(body, BaseMessage.class);
        if (MessageTypeEnum.FINGERPRINT_ENROLL_REPLY.getType().equals(message.getType())) {
            message = JsonUtil.toObject(body, FingerprintEnrollReplyMessage.class);
        } else if (MessageTypeEnum.FINGERPRINT_IDENTIFY_REPLY.getType().equals(message.getType())) {
            message = JsonUtil.toObject(body, FingerprintIdentifyReplyMessage.class);
        } else {
            logger.error("receive reply message with unknown type {}", message.getType());
            throw new BizException(BizResultEnum.MESSAGE_REPLY_UNKNOWN_TYPE);
        }

        Long messageId = createMessage(message.getId(), message);

        logService.logMessageReceive(messageId, message.getId(),
                MessageTypeEnum.getDescriptionByType(message.getType()), body.length());
        logger.info("receive reply {} for message {} successful", messageId, message.getId());

        //释放等待的锁
        CountDownLatch latch = pendingMessageLatchMap.get(message.getId());
        if (latch == null) {
            logger.error("receive reply message for non-pending message {}", message.getId());
            throw new BizException(BizResultEnum.MESSAGE_REPLY_NON_PENDING, message.getId());
        }
        latch.countDown();
    }

    /**
     * 批量查询消息回复，Map出回复消息ID
     *
     * @param ids 消息ID
     * @return 查询结果
     */
    private Map<Long, MessageReplyQueryResult> batchQueryReplyAndMap(List<Long> ids) {
        List<MessageDO> domains = messageMapper.listByReplyId(ids);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("message reply query result is empty");
            return Collections.emptyMap();
        }
        logger.info("query {} message reply(ies)", domains.size());
        return domains.stream()
                .collect(Collectors.toMap(MessageDO::getReplyId,
                        domain -> POJOConvertUtil.convert(domain, MessageReplyQueryResult.class)));
    }

    /**
     * 构造消息中的资源
     *
     * @param image 图像
     * @param voice 声音
     * @return 资源URI的封装
     */
    private BaseMessageResource buildResource(String image, String voice) {
        BaseMessageResource resource = new BaseMessageResource();
        if (!StringUtils.isEmpty(image)) {
            //兼容性需要封装成List
            List<String> images = new ArrayList<>();
            images.add(image);
            resource.setImage(resourceService.getUriByTypeAndName(ResourceTypeEnum.IMAGE, images).get(0));
        }
        if (!StringUtils.isEmpty(voice)) {
            //将文本按标点符号分割，分割后的子句可复用，减少资源数量和体积
            List<String> voices = new ArrayList<>();
            voices.addAll(Arrays.asList(voice.split(RegExpConstant.PUNCTUATION)));
            resource.setVoices(resourceService.getUriByTypeAndName(ResourceTypeEnum.VOICE, voices));
        }
        return resource;
    }

    private Long getSendToSession(MessageSendToParam param) {
        if (param.getId() == null && param.getNumber() == null) {
            logger.error("message send to counter id & number both null");
            throw new BizException(BizResultEnum.MESSAGE_SEND_TO_NULL);
        }
        //只提供了窗口编号的情形：查询窗口ID
        if (param.getId() == null && param.getNumber() != null) {
            Long counterId = counterService.getIdByNumberOptional(param.getNumber())
                    .orElseThrow(() -> {
                        logger.error("message send to counter number {} not exist", param.getNumber());
                        return new BizException(BizResultEnum.MESSAGE_SEND_TO_COUNTER_NOT_EXIST);
                    });
            param.setId(counterId);
        }
        //根据查询窗口ID查询在线会话ID
        return sessionService.getOnlineIdByCounterIdOptional(param.getId())
                .orElseThrow(() -> {
                    logger.error("message send to counter {} session not online", param.getId());
                    return new BizException(BizResultEnum.MESSAGE_SEND_TO_SESSION_NOT_ONLINE);
                });
    }

    /**
     * 创建消息
     *
     * @param counterId 窗口ID
     * @param sessionId 会话ID
     * @param message   消息
     * @return 消息ID
     */
    private Long createMessage(Long counterId, Long sessionId, BaseMessage message) {
        MessageDO domain = new MessageDO();
        domain.setReplyId(0L);
        domain.setCounterId(counterId);
        domain.setSessionId(sessionId);
        domain.setType(message.getType());
        domain.setBody(message.toString());
        domain.setSendTime(DateTimeUtil.getNow());

        if (messageMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create message {} failed", domain.getId());
            throw new BizException(BizResultEnum.MESSAGE_CREATE_ERROR);
        }
        //消息ID需要消息持久化之后才能获得
        Long messageId = domain.getId();
        message.setId(messageId);
        return messageId;
    }

    /**
     * 创建回复消息
     *
     * @param replyId 回复ID
     * @param message 消息
     * @return 消息ID
     */
    private Long createMessage(Long replyId, BaseMessage message) {
        MessageDO domain = new MessageDO();
        domain.setReplyId(replyId);
        domain.setCounterId(0L);
        domain.setSessionId(0L);
        domain.setType(message.getType());
        domain.setBody(message.toString());
        domain.setSendTime(DateTimeUtil.getNow());

        if (messageMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create message {} failed", domain.getId());
            throw new BizException(BizResultEnum.MESSAGE_CREATE_ERROR);
        }

        return domain.getId();
    }

    /**
     * 调用Msg远程服务
     *
     * @param api     API
     * @param request 请求体
     * @return 响应体(JSON)
     */
    private JsonNode callRemote(String api, String request) {
        String url = dependencyConfigItem.getHostMsg() + api;
        logger.info("call downstream url {}", url);
        try {
            JsonNode response = HttpUtils.executeAsJson(url, request);
            logger.info("call downstream msg service get return {}", response);
            return response;
        } catch (IOException e) {
            logger.error("call downstream msg service failed", e);
            throw new BizException(BizResultEnum.MESSAGE_REMOTE_CALL_ERROR);
        }
    }

    /**
     * 调用远程服务发送消息
     *
     * @param token Token
     * @param id    消息ID
     * @param body  消息体
     */
    private void sendRemote(String token, Long id, String body) {
        MessageSendRemoteParam param = new MessageSendRemoteParam();
        param.setToken(token);
        param.setId(id);
        param.setBody(body);

        JsonNode response = callRemote(dependencyConfigItem.getApiMsgMessageSend(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            logger.error("send message to remote get error {}", code);
            throw new BizException(BizResultEnum.MESSAGE_REMOTE_SERVICE_ERROR, code);
        }
    }

    /**
     * 等待阻塞型消息的锁释放或超时
     *
     * @param latch   锁
     * @param timeout 超时时间
     * @return true-锁释放，false-锁超时
     */
    private boolean waitPendingLatch(CountDownLatch latch, Long timeout) {
        try {
            return latch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        return false;
    }

    /**
     * 指纹登记消息发送后，后续处理步骤
     *
     * @param messageId 原消息ID
     * @param latch     阻塞锁
     */
    private void sendFingerprintEnrollPostProcess(Long messageId, CountDownLatch latch) {
        String timeout = settingService.getValueByKeyOptional(SettingEnum.FINGERPRINT_ENROLL_TIMEOUT.getKey())
                .orElse(SettingEnum.FINGERPRINT_ENROLL_TIMEOUT.getDefaultValue());

        //等待超时
        if (!waitPendingLatch(latch, Long.parseLong(timeout))) {
            logger.error("wait fingerprint enroll message {} reply timeout {}", messageId, timeout);
            throw new BizException(BizResultEnum.MESSAGE_REPLY_TIMEOUT);
        }

        //查询回复的消息
        MessageDO domain = messageMapper.getByReplyId(messageId);
        if (domain == null) {
            logger.error("message reply {} not exist", messageId);
            throw new BizException(BizResultEnum.MESSAGE_REPLY_NO_EXIST, messageId);
        }
        String body = domain.getBody();
        FingerprintEnrollReplyMessage message = JsonUtil.toObject(body, FingerprintEnrollReplyMessage.class);

        //调用Fp服务登记回复的指纹模型
        FingerprintEnrollParam param = new FingerprintEnrollParam();
        param.setTemplate(message.getTemplate());
        fingerprintService.enroll(param);
    }

    /**
     * 指纹辨识消息发送后，后续处理步骤
     *
     * @param messageId 原消息ID
     * @param latch     阻塞锁
     * @return 指纹辨识结果
     */
    private MessageFingerprintIdentifyResult sendFingerprintIdentifyPostProcess(Long messageId, CountDownLatch latch) {
        String timeout = settingService.getValueByKeyOptional(SettingEnum.FINGERPRINT_IDENTIFY_TIMEOUT.getKey())
                .orElse(SettingEnum.FINGERPRINT_IDENTIFY_TIMEOUT.getDefaultValue());

        //等待超时
        if (!waitPendingLatch(latch, Long.parseLong(timeout))) {
            logger.error("wait fingerprint identify message {} reply timeout {}", messageId, timeout);
            throw new BizException(BizResultEnum.MESSAGE_REPLY_TIMEOUT);
        }

        //查询回复的消息
        MessageDO domain = messageMapper.getByReplyId(messageId);
        if (domain == null) {
            logger.error("message reply {} not exist", messageId);
            throw new BizException(BizResultEnum.MESSAGE_REPLY_NO_EXIST, messageId);
        }
        String body = domain.getBody();
        FingerprintIdentifyReplyMessage message = JsonUtil.toObject(body, FingerprintIdentifyReplyMessage.class);

        //调用Fp服务辨识回复的指纹模型，反查用户ID和用户编号
        FingerprintIdentifyParam param = new FingerprintIdentifyParam();
        param.setTemplate(message.getTemplate());

        Long userId = fingerprintService.identify(param);
        String userNumber = userService.getNumberByIdOptional(userId).orElse(null);

        MessageFingerprintIdentifyResult result = new MessageFingerprintIdentifyResult();
        result.setId(userId);
        result.setNumber(userNumber);
        return result;
    }

    /**
     * 追加发送指纹登记成功消息
     *
     * @param sendTo 消息接收目标
     */
    private void sendFingerprintEnrollSuccessMessage(MessageSendToParam sendTo) {
        FingerprintEnrollSuccessMessage message = new FingerprintEnrollSuccessMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_SUCCESS,
                MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_SUCCESS));

        Long sessionId = getSendToSession(sendTo);
        Long messageId = createMessage(sendTo.getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, sendTo.getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_ENROLL_SUCCESS.getDescription(), body.length());
        logger.info("send fingerprint enroll success message {} successful", messageId);
    }

    /**
     * 追加发送指纹登记失败消息
     *
     * @param sendTo 消息接收目标
     */
    private void sendFingerprintEnrollFailureMessage(MessageSendToParam sendTo) {
        FingerprintEnrollSuccessMessage message = new FingerprintEnrollSuccessMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_FAILURE,
                MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_FAILURE));

        Long sessionId = getSendToSession(sendTo);
        Long messageId = createMessage(sendTo.getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, sendTo.getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_ENROLL_FAILURE.getDescription(), body.length());
        logger.info("send fingerprint enroll failure message {} successful", messageId);
    }

    /**
     * 追加发送指纹登记失败消息
     *
     * @param sendTo 消息接收目标
     */
    private void sendFingerprintEnrollTimeoutMessage(MessageSendToParam sendTo) {
        FingerprintEnrollSuccessMessage message = new FingerprintEnrollSuccessMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_ENROLL_TIMEOUT,
                MessageResourceNameConstant.VOICE_FINGERPRINT_ENROLL_TIMEOUT));

        Long sessionId = getSendToSession(sendTo);
        Long messageId = createMessage(sendTo.getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, sendTo.getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_ENROLL_TIMEOUT.getDescription(), body.length());
        logger.info("send fingerprint enroll timeout message {} successful", messageId);
    }

    /**
     * 追加发送指纹辨识失败消息
     *
     * @param sendTo 消息接收目标
     */
    private void sendFingerprintIdentifyFailureMessage(MessageSendToParam sendTo) {
        FingerprintIdentifyFailureMessage message = new FingerprintIdentifyFailureMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_IDENTIFY_FAILURE,
                MessageResourceNameConstant.VOICE_FINGERPRINT_IDENTIFY_FAILURE));

        Long sessionId = getSendToSession(sendTo);
        Long messageId = createMessage(sendTo.getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, sendTo.getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_IDENTIFY_FAILURE.getDescription(), body.length());
        logger.info("send fingerprint identify failure message {} successful", messageId);
    }

    /**
     * 追加发送指纹辨识超时消息
     *
     * @param sendTo 消息接收目标
     */
    private void sendFingerprintIdentifyTimeoutMessage(MessageSendToParam sendTo) {
        FingerprintIdentifyTimeoutMessage message = new FingerprintIdentifyTimeoutMessage();
        message.setResource(buildResource(MessageResourceNameConstant.IMAGE_FINGERPRINT_IDENTIFY_TIMEOUT,
                MessageResourceNameConstant.VOICE_FINGERPRINT_IDENTIFY_TIMEOUT));

        Long sessionId = getSendToSession(sendTo);
        Long messageId = createMessage(sendTo.getId(), sessionId, message);

        String token = sessionService.getTokenById(sessionId);
        String body = JsonUtil.toJson(message);
        sendRemote(token, messageId, body);

        logService.logMessageSend(messageId, sendTo.getId(), sessionId,
                MessageTypeEnum.FINGERPRINT_IDENTIFY_TIMEOUT.getDescription(), body.length());
        logger.info("send fingerprint identify timeout message {} successful", messageId);
    }
}
