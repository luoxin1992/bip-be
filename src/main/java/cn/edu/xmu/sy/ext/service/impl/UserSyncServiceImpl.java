/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.UserMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserSyncCreateParam;
import cn.edu.xmu.sy.ext.param.UserSyncDeleteParam;
import cn.edu.xmu.sy.ext.param.UserSyncModifyParam;
import cn.edu.xmu.sy.ext.service.UserService;
import cn.edu.xmu.sy.ext.service.UserSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@Service
public class UserSyncServiceImpl implements UserSyncService {
    private final Logger logger = LoggerFactory.getLogger(UserSyncServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void create(UserSyncCreateParam param) {
        userService.create(POJOConvertUtil.convert(param, UserCreateParam.class), true);
        logger.info("sync create user with number {}", param.getNumber());
    }

    @Override
    public void modify(UserSyncModifyParam param) {
        //同步修改时用编号(而非ID)区别用户，因此编号不能修改
        param.setId(getIdByNumber(param.getNumber()));
        userService.modify(POJOConvertUtil.convert(param, UserModifyParam.class), true);
        logger.info("sync modify user with number {}", param.getNumber());
    }

    @Override
    public void delete(UserSyncDeleteParam param) {
        param.setId(getIdByNumber(param.getNumber()));
        userService.delete(POJOConvertUtil.convert(param, UserDeleteParam.class), true);
        logger.info("sync delete user with number {}", param.getNumber());
    }

    /**
     * 根据用户编号查询ID
     * 用于无本系统内实体ID的第三方系统同步数据
     *
     * @param number 用户编号
     * @return 用户ID
     */
    private Long getIdByNumber(String number) {
        Long id = userMapper.getIdByNumber(number, null);
        if (id == null) {
            logger.error("user with number {} not exist", number);
            throw new BizException(BizResultEnum.USER_NUMBER_NOT_EXIST, number);
        }
        return id;
    }
}
