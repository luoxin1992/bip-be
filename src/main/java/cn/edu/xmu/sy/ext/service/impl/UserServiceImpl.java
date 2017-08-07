/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.UserMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.TtsService;
import cn.edu.xmu.sy.ext.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-11
 */
@CatTransaction
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private FingerprintService fingerprintService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private TtsService ttsService;
    @Autowired
    private LogService logService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public BasePagingResult<UserQueryResult> query(UserQueryParam param) {
        Long count = userMapper.countByParam(param);
        if (count == 0) {
            logger.warn("user query result is empty");
            return new BasePagingResult<>();
        }

        List<UserDO> domains = userMapper.queryByParam(param);
        List<UserQueryResult> results = domains.stream()
                .map(domain -> POJOConvertUtil.convert(domain, UserQueryResult.class))
                .collect(Collectors.toList());

        appendFingerprintQueryResult(results);
        logger.info("query {} of {} user(s)", results.size(), count);

        BasePagingResult<UserQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    public Optional<UserQueryResult> queryByNumber(String number) {
        UserDO domain = userMapper.getByNumber(number);
        return domain != null ? Optional.of(POJOConvertUtil.convert(domain, UserQueryResult.class)) : Optional.empty();
    }

    @Transactional
    @Override
    public void create(UserCreateParam param) {
        if (!param.getFromSync()) {
            checkUserMgrEnable();
        }

        checkNumberDuplicate(param.getNumber(), null);

        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create user {} failed", domain.getId());
            throw new BizException(BizResultEnum.USER_CREATE_ERROR);
        }

        //为新增的用户合成姓名语音
        //TODO 可能语音已存在(同名) 导致重复合成
        ttsService.ttsAsync(param.getName(), false);

        logService.logUserCreate(domain.getId(), domain.getNumber(), domain.getName());
        logger.info("create user {}", domain.getId());
    }

    @Transactional
    @Override
    public void modify(UserModifyParam param) {
        if (!param.getFromSync()) {
            checkUserMgrEnable();
        }

        UserDO before = userMapper.getById(param.getId());
        if (before == null) {
            logger.error("user {} not exist", param.getId());
            throw new BizException(BizResultEnum.USER_NOT_EXIST, param.getId());
        }

        checkNumberDuplicate(param.getNumber(), param.getId());

        UserDO after = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify user {} failed", after.getId());
            throw new BizException(BizResultEnum.USER_MODIFY_ERROR);
        }

        //当姓名被修改时重新合成姓名语音
        if (!Objects.equals(before.getName(), after.getName())) {
            ttsService.ttsAsync(param.getName(), false);
        }

        logService.logUserModify(after.getId(), POJOCompareUtil.compare(UserDO.class, before, after));
        logger.info("modify user {}", after.getId());
    }

    @Transactional
    @Override
    public void delete(UserDeleteParam param) {
        if (!param.getFromSync()) {
            checkUserMgrEnable();
        }

        //删除用户前先删除关联的指纹
        fingerprintService.deleteByUser(param.getId());

        if (userMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete user {} failed", param.getId());
            throw new BizException(BizResultEnum.USER_DELETE_ERROR);
        }

        logService.logUserDelete(param.getId());
        logger.info("delete user {}", param.getId());
    }

    /**
     * 向用户查询结果中追加指纹查询结果
     *
     * @param results 用户查询结果
     */
    private void appendFingerprintQueryResult(List<UserQueryResult> results) {
        List<Long> userIds = results.stream()
                .map(UserQueryResult::getId)
                .collect(Collectors.toList());

        Map<Long, List<FingerprintQueryResult>> fingerprints = fingerprintService.queryBatchAndGroup(userIds);
        results.forEach(result -> result.setFingerprints(fingerprints.get(result.getId())));
    }

    /**
     * 检查参数设置中的启用用户管理功能
     */
    private void checkUserMgrEnable() {
        String userMgrEnable = settingService.getValueByKeyOrDefault(SettingEnum.MISC_USER_MGR_ENABLE);
        if (!CommonConstant.TRUE.equals(userMgrEnable)) {
            logger.error("user management is disabled");
            throw new BizException(BizResultEnum.USER_MGR_DISABLE);
        }
    }

    /**
     * 检查用户编号是否重复
     *
     * @param number  编号
     * @param exclude 排除ID
     */
    private void checkNumberDuplicate(String number, Long exclude) {
        UserDO domain = userMapper.getByNumber(number);
        if (domain != null && !Objects.equals(domain.getId(), exclude)) {
            logger.error("user number {} duplicate with {}", number, domain.getId());
            throw new BizException(BizResultEnum.USER_NUMBER_DUPLICATE, number);
        }
    }
}
