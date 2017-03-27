/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.UserMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.FingerprintBatchQueryParam;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.UserService;
import org.apache.commons.collections.CollectionUtils;
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

/**
 * @author luoxin
 * @version 2017-3-11
 */
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private FingerprintService fingerprintService;
    @Autowired
    private LogService logService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 列表查询
     *
     * @param param 查询参数
     * @return 查询结果
     */
    @Override
    public BasePagingResult<UserQueryResult> list(UserQueryParam param) {
        long count = userMapper.countByParam(param);
        Map<Long, UserQueryResult> users = queryUser(param);
        Map<Long, List<FingerprintQueryResult>> fingerprints = queryFingerprint(new ArrayList<>(users.keySet()));
        for (UserQueryResult user : users.values()) {
            user.setFingerprints(fingerprints.get(user.getId()));
        }
        logger.info("query {} users", users.size());

        BasePagingResult<UserQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(new ArrayList<>(users.values()));
        return result;
    }


    @Transactional
    @Override
    public void create(UserCreateParam param) {
        if (userMapper.getIdByNumber(param.getNumber()) != null) {
            throw new BizException(BizResultEnum.USER_NUMBER_DUPLICATE_ERROR, param.getNumber());
        }
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.save(domain) != 1) {
            throw new BizException(BizResultEnum.USER_CREATE_ERROR);
        }
        logger.info("create user with id {}", domain.getId());
    }

    @Transactional
    @Override
    public void modify(UserModifyParam param) {
        UserDO before = userMapper.getById(param.getId());
        if (before == null) {
            throw new BizException(BizResultEnum.USER_NOT_EXIST_ERROR, param.getNumber());
        }
        if (param.getNumber() != null) {
            if (userMapper.getIdByNumber(param.getNumber()) != null) {
                throw new BizException(BizResultEnum.USER_NUMBER_DUPLICATE_ERROR, param.getNumber());
            }
        }
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.updateById(domain) != 1) {
            throw new BizException(BizResultEnum.USER_UPDATE_ERROR);
        }
        logger.info("modify user with id {}", domain.getId());
    }

    @Transactional
    @Override
    public void delete(Long id) {
        //删除用户前先删除已登记的指纹
        fingerprintService.deleteByUserId(id);
        if (userMapper.removeById(id) != 1) {
            throw new BizException(BizResultEnum.USER_DELETE_ERROR);
        }
        logger.info("delete user with id {}", id);
    }

    private Map<Long, UserQueryResult> queryUser(UserQueryParam param) {
        List<UserDO> domains = userMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyMap();
        }

        Map<Long, UserQueryResult> retMap = new LinkedHashMap<>();
        for (UserDO domain : domains) {
            retMap.put(domain.getId(), POJOConvertUtil.convert(domain, UserQueryResult.class));
        }
        return retMap;
    }

    private Map<Long, List<FingerprintQueryResult>> queryFingerprint(List<Long> userIds) {
        FingerprintBatchQueryParam param = new FingerprintBatchQueryParam();
        param.setUserIds(userIds);

        List<FingerprintQueryResult> results = fingerprintService.queryBatch(param);
        if (CollectionUtils.isEmpty(results)) {
            return Collections.emptyMap();
        }

        Map<Long, List<FingerprintQueryResult>> retMap = new LinkedHashMap<>();
        for (FingerprintQueryResult result : results) {
            if (!retMap.containsKey(result.getUserId())) {
                retMap.put(result.getUserId(), new ArrayList<>());
            }
            retMap.get(result.getUserId()).add(result);
        }
        return retMap;
    }
}
