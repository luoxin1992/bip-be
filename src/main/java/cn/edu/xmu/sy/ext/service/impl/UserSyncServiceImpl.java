/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.UserMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.param.UserSyncParam;
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
    private UserMapper userMapper;

    @Override
    public void create(UserSyncParam param) {
        Long id = userMapper.getIdByNumber(param.getNumber());
        if (id != null) {
            //创建的用户编号重复
            throw new BizException(BizResultEnum.USER_NUMBER_DUPLICATE_ERROR, param.getNumber());
        }
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        userMapper.save(domain);
    }

    @Override
    public void modify(UserSyncParam param) {
        Long id = userMapper.getIdByNumber(param.getNumber());
        if (id == null) {
            //修改的用户不存在
            throw new BizException(BizResultEnum.USER_NUMBER_NOT_EXIST_ERROR, param.getNumber());
        }
        //同步数据的逻辑不能修改编号，也不需要校验编号是否重复
        UserDO domain = POJOConvertUtil.convert(param, UserDO.class);
        domain.setId(id);
        domain.setNumber(null);
        userMapper.updateById(domain);
    }

    @Override
    public void delete(UserSyncParam param) {
        Long id = userMapper.getIdByNumber(param.getNumber());
        if (id == null) {
            //删除的用户不存在
            logger.warn("fail to delete a not existed user");
            throw new BizException(BizResultEnum.USER_NUMBER_NOT_EXIST_ERROR, param.getNumber());
        }
        userMapper.removeById(id);
        logger.info("delete user with number {} by synchronize", param.getNumber());
    }
}
