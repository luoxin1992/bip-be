/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
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
import cn.edu.xmu.sy.ext.result.FingerprintTemplateQueryResult;
import cn.edu.xmu.sy.ext.result.UserListSimpleResult;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public BaseListResult<UserListSimpleResult> listSimple() {
        Long count = userMapper.countAll();
        if (count == 0) {
            logger.warn("user simple list result is empty");
            return new BaseListResult<>();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("user simple list will contain {} result(s) and divide to {} page(s)", count, pages);

        List<UserListSimpleResult> results = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            //分段(页)查询用户
            List<UserDO> domains = userMapper.listByPaging((long) (i * rows), rows);
            //取当前段(页)用户ID查询指纹
            List<Long> userIds = domains.stream()
                    .map(UserDO::getId)
                    .collect(Collectors.toList());
            Map<Long, List<FingerprintTemplateQueryResult>> fingerprints = fingerprintService
                    .queryTemplateBatchAndGroup(userIds);
            //转换成Result，加入指纹模型并过滤掉没有登记指纹的用户
            List<UserListSimpleResult> users = domains.stream()
                    .map(domain -> POJOConvertUtil.convert(domain, UserListSimpleResult.class))
                    .peek(result -> result.setFingerprints(fingerprints.get(result.getId())))
                    .filter(result -> !CollectionUtils.isEmpty(result.getFingerprints()))
                    .collect(Collectors.toList());
            //将当前段(页)添加回总的结果集中
            results.addAll(users);
        }
        logger.info("simple list {} user(s)", results.size());

        BaseListResult<UserListSimpleResult> retResult = new BaseListResult<>();
        retResult.setTotal(results.size());
        retResult.setList(results);
        return retResult;
    }

    @Override
    public BasePagingResult<UserQueryResult> query(UserQueryParam param) {
        long count = userMapper.countByParam(param);
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

    @Transactional
    @Override
    public void create(UserCreateParam param, boolean fromSync) {
        if (!fromSync) {
            checkUserMgrEnable();
        }
        checkNumberDuplicate(param.getNumber(), null);
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create user with id {} failed", domain.getId());
            throw new BizException(BizResultEnum.USER_CREATE_ERROR);
        }
        logService.logUserCreate(domain);
        logger.info("create user with id {} successful", domain.getId());
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
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        if (userMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify user with id {} failed", domain.getId());
            throw new BizException(BizResultEnum.USER_UPDATE_ERROR);
        }
        logService.logUserModify(before, domain);
        logger.info("modify user with id {} successful", domain.getId());
    }

    @Transactional
    @Override
    public void delete(UserDeleteParam param, boolean fromSync) {
        if (!fromSync) {
            checkUserMgrEnable();
        }
        //删除用户前先删除已登记的指纹
        fingerprintService.deleteByUserId(param.getId());
        if (userMapper.removeById(param.getId()) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete user with id {} failed", param.getId());
            throw new BizException(BizResultEnum.USER_DELETE_ERROR);
        }
        logService.logUserDelete(param.getId());
        logger.info("delete user with id {} successful", param.getId());
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
