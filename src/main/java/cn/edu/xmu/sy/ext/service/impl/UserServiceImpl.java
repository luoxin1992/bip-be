/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BasePagingResult;
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
import cn.edu.xmu.sy.ext.result.UserQuerySimpleResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private SettingService settingService;
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

        List<Long> userIds = domains.stream()
                .map(UserDO::getId)
                .collect(Collectors.toList());
        Map<Long, List<FingerprintQueryResult>> fingerprints = fingerprintService.queryBatchAndGroup(userIds);

        List<UserQueryResult> users = domains.stream()
                .map(domain -> POJOConvertUtil.convert(domain, UserQueryResult.class))
                .peek(user -> user.setFingerprints(fingerprints.get(user.getId())))
                .collect(Collectors.toList());
        logger.info("query {}/{} user(s)", users.size(), count);

        BasePagingResult<UserQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(users);
        return result;
    }

    @Override
    public UserQuerySimpleResult queryById(Long id) {
        UserDO domain = userMapper.getById(id);
        if (domain == null) {
            logger.error("user {} not exist", id);
            throw new BizException(BizResultEnum.USER_NOT_EXIST);
        }
        return POJOConvertUtil.convert(domain, UserQuerySimpleResult.class);
    }

    @Transactional
    @Override
    public void create(UserCreateParam param, boolean fromSync) {
        if (!fromSync) {
            checkUserMgrEnable();
        }
        checkNumberDuplicate(param.getNumber(), null);

        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create user {} failed", domain.getId());
            throw new BizException(BizResultEnum.USER_CREATE_ERROR);
        }

        logService.logUserCreate(domain.getId(), domain.getNumber(), domain.getName());
        logger.info("create user {} successful", domain.getId());
    }

    @Transactional
    @Override
    public void modify(UserModifyParam param, boolean fromSync) {
        if (!fromSync) {
            checkUserMgrEnable();
        }

        UserDO before = userMapper.getById(param.getId());
        if (before == null) {
            throw new BizException(BizResultEnum.USER_NOT_EXIST, param.getId());
        }

        checkNumberDuplicate(param.getNumber(), param.getId());

        UserDO after = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify user {} failed", after.getId());
            throw new BizException(BizResultEnum.USER_MODIFY_ERROR);
        }

        logService.logUserModify(after.getId(), POJOCompareUtil.compare(UserDO.class, before, after));
        logger.info("modify user {} successful", after.getId());
    }

    @Transactional
    @Override
    public void delete(UserDeleteParam param, boolean fromSync) {
        if (!fromSync) {
            checkUserMgrEnable();
        }

        //删除用户前先删除已登记的指纹
        fingerprintService.deleteByUser(param.getId());

        if (userMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete user {} failed", param.getId());
            throw new BizException(BizResultEnum.USER_DELETE_ERROR);
        }

        logService.logUserDelete(param.getId());
        logger.info("delete user {} successful", param.getId());
    }

    /**
     * 检查参数设置中的启用用户管理功能
     */
    private void checkUserMgrEnable() {
        String userMgrEnable = settingService.getValueByKeyOptional(SettingEnum.MISC_USER_MGR_ENABLE.getKey())
                .orElse(SettingEnum.MISC_USER_MGR_ENABLE.getDefaultValue());
        if (CommonConstant.FALSE.equals(userMgrEnable)) {
            logger.error("user management is disable");
            throw new BizException(BizResultEnum.USER_MGR_DISABLE);
        }
    }

    /**
     * 检查用户编号是否重复
     *
     * @param number 用户编号
     */
    private void checkNumberDuplicate(String number, Long exclude) {
        if (userMapper.getIdByNumber(number, exclude) != null) {
            logger.error("user with number {} duplicate", number);
            throw new BizException(BizResultEnum.USER_NUMBER_DUPLICATE, number);
        }
    }
}
