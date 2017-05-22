/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.constant.BaseFieldNameConstant;
import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.HttpUtils;
import cn.com.lx1992.lib.util.JsonUtil;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.config.DependencyConfigItem;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.FingerprintMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.FingerprintFingerEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteRemoteParam;
import cn.edu.xmu.sy.ext.param.FingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.FingerprintEnrollRemoteParam;
import cn.edu.xmu.sy.ext.param.FingerprintFingerListParam;
import cn.edu.xmu.sy.ext.param.FingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.FingerprintIdentifyRemoteParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.result.FingerprintFingerListResult;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.result.FingerprintTemplateListResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@Service
public class FingerprintServiceImpl implements FingerprintService {
    private final Logger logger = LoggerFactory.getLogger(FingerprintServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private LogService logService;

    @Autowired
    private FingerprintMapper fingerprintMapper;

    @Autowired
    private DependencyConfigItem dependencyConfigItem;

    @Override
    @Transactional
    public void modify(FingerprintModifyParam param) {
        FingerprintDO before = fingerprintMapper.getById(param.getId());
        if (before == null) {
            logger.error("fingerprint {} not exist", param.getId());
            throw new BizException(BizResultEnum.FINGERPRINT_NOT_EXIST, param.getId());
        }

        FingerprintDO after = POJOConvertUtil.convert(param, FingerprintDO.class);
        if (fingerprintMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify fingerprint {} failed", param.getId());
            throw new BizException(BizResultEnum.FINGERPRINT_MODIFY_ERROR);
        }

        logService.logFingerprintModify(before.getId(), POJOCompareUtil.compare(FingerprintDO.class, before, after));
        logger.info("modify fingerprint {}", param.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        FingerprintDO domain = fingerprintMapper.getById(id);
        if (domain == null) {
            logger.error("fingerprint {} not exist", id);
            throw new BizException(BizResultEnum.FINGERPRINT_NOT_EXIST, id);
        }

        if (fingerprintMapper.removeById(id) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete fingerprint {} failed", id);
            throw new BizException(BizResultEnum.FINGERPRINT_DELETE_ERROR);
        }

        deleteRemote(domain.getUuid());

        logService.logFingerprintDelete(id);
        logger.info("delete fingerprint {}", id);
    }

    @Override
    @Transactional
    public void deleteByUser(Long userId) {
        List<FingerprintDO> domains = fingerprintMapper.getByUserId(userId);
        if (CollectionUtils.isEmpty(domains)) {
            logger.error("fingerprint for user {} not exist", userId);
            throw new BizException(BizResultEnum.FINGERPRINT_USER_NOT_EXIST);
        }

        if (fingerprintMapper.removeByUserId(userId) != domains.size()) {
            logger.error("delete fingerprint by user {} failed", userId);
            throw new BizException(BizResultEnum.FINGERPRINT_DELETE_ERROR);
        }

        domains.forEach(domain -> deleteRemote(domain.getUuid()));

        logService.logFingerprintDeleteByUser(userId);
        logger.info("delete {} fingerprint(s) for user {}", domains.size(), userId);
    }

    @Override
    public Long countByUser(Long userId) {
        Long count = fingerprintMapper.countByUserId(userId);
        logger.info("user {} has {} fingerprint(s)", userId, count);
        return count;
    }

    @Override
    @Transactional
    public void enroll(FingerprintEnrollParam param) {
        checkMaxEnrollCount(param.getUserId());

        FingerprintDO domain = POJOConvertUtil.convert(param, FingerprintDO.class);
        domain.setUuid(UUIDUtil.randomHexUUID());
        if (fingerprintMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            throw new BizException(BizResultEnum.FINGERPRINT_ENROLL_ERROR);
        }

        enrollRemote(domain.getUuid(), domain.getTemplate());

        logService.logFingerprintEnroll(domain.getId(), domain.getUserId());
        logger.info("enroll fingerprint {} for user {}", domain.getId(), domain.getUserId());
    }

    @Override
    @Transactional
    public Long identify(FingerprintIdentifyParam param) {
        String uuid = identifyRemote(param.getTemplate());

        //根据UUID反查指纹
        FingerprintDO domain = fingerprintMapper.getByUuid(uuid);
        if (domain == null) {
            logger.error("fingerprint no enroll");
            throw new BizException(BizResultEnum.FINGERPRINT_NO_ENROLL);
        }

        domain.setIdentifyTime(DateTimeUtil.getNow());
        if (fingerprintMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("identify fingerprint failed");
            throw new BizException(BizResultEnum.FINGERPRINT_IDENTIFY_ERROR);
        }

        logService.logFingerprintIdentify(domain.getId(), domain.getUserId());
        logger.info("identify fingerprint {} for user {} successful", domain.getId(), domain.getUserId());
        return domain.getUserId();
    }

    @Override
    public FingerprintFingerListResult listFinger() {
        List<String> fingers = Arrays.stream(FingerprintFingerEnum.values())
                .filter(value -> value != FingerprintFingerEnum.UNKNOWN)
                .map(FingerprintFingerEnum::getFinger)
                .collect(Collectors.toList());

        FingerprintFingerListResult result = new FingerprintFingerListResult();
        result.setFingers(fingers);
        return result;
    }

    @Override
    public FingerprintFingerListResult listFingerUsable(FingerprintFingerListParam param) {
        List<String> allFingers = listFinger().getFingers();
        List<String> usedFingers = fingerprintMapper.getByUserId(param.getUserId()).stream()
                .map(FingerprintDO::getFinger)
                .collect(Collectors.toList());

        List<String> usableFingers = new ArrayList<>(CollectionUtils.subtract(allFingers, usedFingers));
        if (CollectionUtils.isEmpty(usableFingers)) {
            logger.error("no usable finger for user {}", param.getUserId());
            throw new BizException(BizResultEnum.FINGERPRINT_NO_USABLE_FINGER);
        }
        logger.info("list {} usable finger(s) for user {}", usableFingers.size(), param.getUserId());

        FingerprintFingerListResult result = new FingerprintFingerListResult();
        result.setFingers(usableFingers);
        return result;
    }

    @Override
    public BaseListResult<FingerprintTemplateListResult> listTemplate() {
        Long count = fingerprintMapper.countAll();
        if (count == 0) {
            logger.warn("fingerprint template list result is empty");
            return new BaseListResult<>();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("fingerprint template list contain {} result(s) and divide to {} page(s)", count, pages);

        List<FingerprintTemplateListResult> allResult = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            //分段(页)查询指纹模型
            List<FingerprintDO> domains = fingerprintMapper.listByPaging((long) (i * rows), rows);
            //转换成Result，并添加回总的结果集中
            List<FingerprintTemplateListResult> results = domains.stream()
                    .map(domain -> POJOConvertUtil.convert(domain, FingerprintTemplateListResult.class))
                    .collect(Collectors.toList());
            allResult.addAll(results);
        }
        logger.info("list {} fingerprint template(s)", allResult.size());

        BaseListResult<FingerprintTemplateListResult> retResult = new BaseListResult<>();
        retResult.setTotal(allResult.size());
        retResult.setList(allResult);
        return retResult;
    }

    @Override
    public List<FingerprintQueryResult> query(Long userId) {
        List<FingerprintDO> domains = fingerprintMapper.getByUserId(userId);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint query result is empty");
            return Collections.emptyList();
        }

        logger.info("query {} fingerprints for user {}", domains.size(), userId);
        return domains.stream()
                .map((domain) -> POJOConvertUtil.convert(domain, FingerprintQueryResult.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FingerprintQueryResult> queryBatch(List<Long> userIds) {
        List<FingerprintDO> domains = queryBatchRaw(userIds);
        return domains.stream()
                .map((domain) -> POJOConvertUtil.convert(domain, FingerprintQueryResult.class))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<FingerprintQueryResult>> queryBatchAndGroup(List<Long> userIds) {
        List<FingerprintDO> domains = queryBatchRaw(userIds);
        return domains.stream()
                .collect(Collectors.groupingBy(FingerprintDO::getUserId, TreeMap::new,
                        Collectors.mapping(domain -> POJOConvertUtil.convert(domain, FingerprintQueryResult.class),
                                Collectors.toList())));
    }

    /**
     * 检查用户登记指纹数是否已经达到上限
     *
     * @param userId 用户ID
     */
    private void checkMaxEnrollCount(Long userId) {
        Integer count = Math.toIntExact(fingerprintMapper.countByUserId(userId));
        String max = settingService.getValueByKeyOptional(SettingEnum.FINGERPRINT_MAX_ENROLL_COUNT.getKey())
                .orElse(SettingEnum.FINGERPRINT_MAX_ENROLL_COUNT.getDefaultValue());

        if (count >= Integer.parseInt(max)) {
            logger.error("user {} already enroll {} fingerprint(s)", userId, count);
            throw new BizException(BizResultEnum.FINGERPRINT_ENROLL_COUNT_OVERRUN);
        }
    }

    /**
     * 调用远程服务删除指纹
     *
     * @param uuid UUID
     */
    private void deleteRemote(String uuid) {
        FingerprintDeleteRemoteParam param = new FingerprintDeleteRemoteParam();
        param.setUuid(uuid);
        JsonNode response = callRemote(dependencyConfigItem.getApiFpRemove(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            logger.error("delete fingerprint from remote get error {}", code);
            throw new BizException(BizResultEnum.FINGERPRINT_REMOTE_SERVICE_ERROR, code);
        }
    }

    /**
     * 调用远程服务登记指纹
     *
     * @param uuid     UUID
     * @param template 指纹模板
     */
    private void enrollRemote(String uuid, String template) {
        FingerprintEnrollRemoteParam param = new FingerprintEnrollRemoteParam();
        param.setUuid(uuid);
        param.setTemplate(template);
        JsonNode response = callRemote(dependencyConfigItem.getApiFpEnroll(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            if (code == BizResultEnum.SERVICE_FP_TEMPLATE_DUPLICATE.getCode()) {
                logger.error("fingerprint already enroll");
                throw new BizException(BizResultEnum.FINGERPRINT_ALREADY_ENROLL);
            } else {
                logger.error("enroll fingerprint from remote get error {}", code);
                throw new BizException(BizResultEnum.FINGERPRINT_REMOTE_SERVICE_ERROR, code);
            }
        }
    }

    /**
     * 调用远程服务辨识指纹
     *
     * @param template 指纹模板
     * @return UUID
     */
    private String identifyRemote(String template) {
        FingerprintIdentifyRemoteParam param = new FingerprintIdentifyRemoteParam();
        param.setTemplate(template);
        JsonNode response = callRemote(dependencyConfigItem.getApiFpIdentify(), JsonUtil.toJson(param));

        int code = response.get(BaseFieldNameConstant.CODE).asInt();
        if (code != BaseResultEnum.OK.getCode()) {
            if (code == BizResultEnum.SERVICE_FP_IDENTIFY_ERROR.getCode()) {
                logger.error("fingerprint mismatch any user");
                throw new BizException(BizResultEnum.FINGERPRINT_IDENTIFY_MISMATCH);
            } else {
                logger.error("identify fingerprint from remote get error {}", code);
                throw new BizException(BizResultEnum.FINGERPRINT_REMOTE_SERVICE_ERROR, code);
            }
        }
        return response.get(BaseFieldNameConstant.RESULT).get("uuid").asText();
    }

    /**
     * 调用远程服务
     *
     * @param api     API
     * @param request 请求体
     * @return 响应体(JSON)
     */
    private JsonNode callRemote(String api, String request) {
        String url = dependencyConfigItem.getHostFp() + api;
        logger.info("call downstream url {}", url);
        try {
            JsonNode response = HttpUtils.executeAsJson(url, request);
            logger.info("call downstream service get return {}", response);
            return response;
        } catch (IOException e) {
            logger.error("call downstream service failed", e);
            throw new BizException(BizResultEnum.FINGERPRINT_REMOTE_CALL_ERROR);
        }
    }

    /**
     * 批量查询，返回原始Domain
     *
     * @param userIds 用户ID
     * @return 查询结果
     */
    private List<FingerprintDO> queryBatchRaw(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            logger.warn("parameter is empty, abort");
            return Collections.emptyList();
        }

        List<FingerprintDO> domains = fingerprintMapper.listByUserId(userIds);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint batch query result is empty");
            return Collections.emptyList();
        }

        logger.info("batch query {} fingerprint(s) for {} user(s)", domains.size(), userIds.size());
        return domains;
    }
}
