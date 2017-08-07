/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.com.lx1992.lib.util.UIDGenerateUtil;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.FingerprintMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.FingerprintFingerEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.FingerprintListFingerParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.param.FingerprintQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintListFingerResult;
import cn.edu.xmu.sy.ext.result.FingerprintListTemplateResult;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.service.FingerprintSdkService;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@CatTransaction
@Service
public class FingerprintServiceImpl implements FingerprintService {
    private final Logger logger = LoggerFactory.getLogger(FingerprintServiceImpl.class);

    @Autowired
    private FingerprintSdkService fingerprintSdkService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private LogService logService;

    @Autowired
    private FingerprintMapper fingerprintMapper;

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
    public void delete(FingerprintDeleteParam param) {
        FingerprintDO domain = fingerprintMapper.getById(param.getId());
        if (domain == null) {
            logger.error("fingerprint {} not exist", param.getId());
            throw new BizException(BizResultEnum.FINGERPRINT_NOT_EXIST, param.getId());
        }

        if (fingerprintMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete fingerprint {} failed", param.getId());
            throw new BizException(BizResultEnum.FINGERPRINT_DELETE_ERROR);
        }

        //删除指纹仪SDK缓存
        fingerprintSdkService.remove(domain.getUid());

        logService.logFingerprintDelete(param.getId());
        logger.info("delete fingerprint {}", param.getId());
    }

    @Override
    @Transactional
    public void deleteByUser(Long userId) {
        List<FingerprintDO> domains = fingerprintMapper.getByUserId(userId);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("no fingerprint for user {}", userId);
            return;
        }

        if (fingerprintMapper.removeByUserId(userId) != domains.size()) {
            logger.error("delete fingerprint for user {} failed", userId);
            throw new BizException(BizResultEnum.FINGERPRINT_DELETE_ERROR);
        }

        domains.forEach(domain -> fingerprintSdkService.remove(domain.getUid()));

        logService.logFingerprintDeleteByUser(userId);
        logger.info("delete {} fingerprint(s) for user {}", domains.size(), userId);
    }

    @Override
    @Transactional
    public void enroll(Long userId, String finger, String template) {
        checkEnrollMaxCount(userId);

        FingerprintDO domain = new FingerprintDO();
        domain.setUserId(userId);
        //指纹仪SDK仅支持int型ID，故生成一个32位UID
        domain.setUid(UIDGenerateUtil.Compact.nextId());
        domain.setFinger(finger);
        domain.setTemplate(template);
        domain.setEnrollTime(DateTimeUtil.getNow());

        if (fingerprintMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("enroll fingerprint for user {} failed", userId);
            throw new BizException(BizResultEnum.FINGERPRINT_ENROLL_ERROR);
        }

        //登记到指纹仪SDK缓存
        fingerprintSdkService.enroll(domain.getUid(), domain.getTemplate(), true);

        logService.logFingerprintEnroll(domain.getId(), domain.getUserId(), domain.getUid());
        logger.info("enroll fingerprint {} for user {}", domain.getId(), domain.getUserId());
    }

    @Override
    @Transactional
    public Long identify(String template) {
        //从指纹仪SDK缓存辨识指纹模板
        Integer uid = fingerprintSdkService.identify(template);

        //根据UID反查指纹
        FingerprintDO domain = fingerprintMapper.getByUid(uid);
        if (domain == null) {
            logger.error("fingerprint with uid {} not exist", uid);
            throw new BizException(BizResultEnum.FINGERPRINT_UID_NOT_EXIST, uid);
        }

        //更新辨识时间
        FingerprintDO domain1 = new FingerprintDO();
        domain1.setId(domain.getId());
        domain1.setIdentifyTime(DateTimeUtil.getNow());
        if (fingerprintMapper.updateById(domain1) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("identify fingerprint {} failed", domain1.getId());
            throw new BizException(BizResultEnum.FINGERPRINT_IDENTIFY_ERROR);
        }

        logService.logFingerprintIdentify(domain.getId(), domain.getUserId(), domain.getUid());
        logger.info("identify fingerprint {} for user {}", domain.getId(), domain.getUserId());

        return domain.getUserId();
    }

    @Override
    public FingerprintListFingerResult listFinger(FingerprintListFingerParam param) {
        List<String> all = Arrays.stream(FingerprintFingerEnum.values())
                .filter(value -> value != FingerprintFingerEnum.UNKNOWN)
                .map(FingerprintFingerEnum::getFinger)
                .collect(Collectors.toList());
        List<String> used = fingerprintMapper.getByUserId(param.getUserId()).stream()
                .map(FingerprintDO::getFinger)
                .collect(Collectors.toList());

        //全部手指和已用手指的差集即可用手指
        List<String> usable = new ArrayList<>(CollectionUtils.subtract(all, used));

        logger.info("list {} usable finger(s) for user {}", usable.size(), param.getUserId());
        FingerprintListFingerResult result = new FingerprintListFingerResult();
        result.setFingers(usable);
        return result;
    }

    @Override
    public BaseListResult<FingerprintListTemplateResult> listTemplate() {
        Long count = fingerprintMapper.countAll();
        if (count == 0) {
            logger.warn("fingerprint template list result is empty");
            return new BaseListResult<>();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("fingerprint template list contain {} result(s) and divide to {} page(s)", count, pages);

        List<FingerprintListTemplateResult> all = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            //分段(页)查询指纹模型
            List<FingerprintDO> domains = fingerprintMapper.listAll((long) (i * rows), rows);
            //转换成Result，并添加回总的结果集中
            List<FingerprintListTemplateResult> results = domains.stream()
                    .map(domain -> POJOConvertUtil.convert(domain, FingerprintListTemplateResult.class))
                    .collect(Collectors.toList());
            all.addAll(results);
        }
        logger.info("list {} fingerprint template(s)", all.size());

        BaseListResult<FingerprintListTemplateResult> result = new BaseListResult<>();
        result.setTotal(all.size());
        result.setList(all);
        return result;
    }

    @Override
    public BaseListResult<FingerprintQueryResult> query(FingerprintQueryParam param) {
        List<FingerprintDO> domains = fingerprintMapper.getByUserId(param.getUserId());
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint for user {} query result is empty", param.getUserId());
            return new BaseListResult<>();
        }

        List<FingerprintQueryResult> results = domains.stream()
                .map((domain) -> POJOConvertUtil.convert(domain, FingerprintQueryResult.class))
                .collect(Collectors.toList());
        logger.info("query {} fingerprint(s) for user {}", domains.size(), param.getUserId());

        BaseListResult<FingerprintQueryResult> result = new BaseListResult<>();
        result.setTotal(results.size());
        result.setList(results);
        return result;
    }

    @Override
    public Map<Long, List<FingerprintQueryResult>> queryBatchAndGroup(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            logger.warn("empty user id list in query param");
            return Collections.emptyMap();
        }

        List<FingerprintDO> domains = fingerprintMapper.listByUserId(userIds);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint batch query result is empty");
            return Collections.emptyMap();
        }

        logger.info("batch query {} fingerprint(s) for {} user(s)", domains.size(), userIds.size());
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
    private void checkEnrollMaxCount(Long userId) {
        Long current = fingerprintMapper.countByUserId(userId);
        String max = settingService.getValueByKeyOrDefault(SettingEnum.FINGERPRINT_ENROLL_MAX_COUNT);

        if (current >= Integer.parseInt(max)) {
            logger.error("user {} already enroll {} fingerprint(s)", userId, current);
            throw new BizException(BizResultEnum.FINGERPRINT_ENROLL_MAX_COUNT);
        }
    }
}
