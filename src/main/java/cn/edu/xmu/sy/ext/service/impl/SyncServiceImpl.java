/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.FingerprintFingerEnum;
import cn.edu.xmu.sy.ext.param.SyncFingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.SyncUserCreateParam;
import cn.edu.xmu.sy.ext.param.SyncUserDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncUserModifyParam;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.SyncService;
import cn.edu.xmu.sy.ext.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@CatTransaction
@Service
public class SyncServiceImpl implements SyncService {
    private final Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FingerprintService fingerprintService;
    @Autowired
    private LogService logService;

    @Override
    @Transactional
    public void userCreate(SyncUserCreateParam param) {
        UserCreateParam param1 = POJOConvertUtil.convert(param, UserCreateParam.class);
        param1.setFromSync(true);

        userService.create(param1);
        logService.logSyncUserCreate(param.getNumber());
        logger.info("sync create user with number {}", param.getNumber());
    }

    @Override
    @Transactional
    public void userModify(SyncUserModifyParam param) {
        //同步修改时用编号而非ID区别用户，因此编号不能修改
        UserModifyParam param1 = POJOConvertUtil.convert(param, UserModifyParam.class);
        param1.setId(getIdByNumber(param.getNumber()));
        param1.setNumber(null);
        param1.setFromSync(true);

        userService.modify(param1);
        logService.logSyncUserModify(param.getNumber());
        logger.info("sync modify user with number {}", param.getNumber());
    }

    @Override
    @Transactional
    public void userDelete(SyncUserDeleteParam param) {
        UserDeleteParam param1 = new UserDeleteParam();
        param1.setId(getIdByNumber(param.getNumber()));
        param1.setFromSync(true);

        userService.delete(param1);
        logService.logSyncUserDelete(param.getNumber());
        logger.info("sync delete user with number {}", param.getNumber());
    }

    @Override
    @Transactional
    public void fingerprintEnroll(SyncFingerprintEnrollParam param) {
        Long userId = getIdByNumber(param.getNumber());

        fingerprintService.enroll(userId, FingerprintFingerEnum.UNKNOWN.getFinger(), param.getTemplate());
        logService.logSyncFingerprintEnroll(param.getNumber());
        logger.info("sync enroll fingerprint for user {}", userId);
    }

    @Override
    @Transactional
    public void fingerprintDelete(SyncFingerprintDeleteParam param) {
        Long userId = getIdByNumber(param.getNumber());

        fingerprintService.deleteByUser(userId);
        logService.logSyncFingerprintDelete(param.getNumber());
        logger.info("sync delete fingerprint for user {}", userId);
    }

    /**
     * 根据用户编号查询用户ID
     *
     * @param number 用户
     * @return 用户ID
     */
    private Long getIdByNumber(String number) {
        return userService.queryByNumber(number)
                .orElseThrow(() -> {
                    logger.error("sync user with number {} not exist", number);
                    throw new BizException(BizResultEnum.SYNC_USER_NUMBER_NOT_EXIST, number);
                })
                .getId();
    }
}
