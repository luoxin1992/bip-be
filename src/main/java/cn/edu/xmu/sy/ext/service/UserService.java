/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.result.UserQuerySimpleResult;

/**
 * 用户Service
 *
 * @author luoxin
 * @version 2017-3-17
 */
public interface UserService {
    /**
     * 查询用户
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<UserQueryResult> query(UserQueryParam param);

    /**
     * 查询用户(根据ID)(简版结果)
     *
     * @param id 用户ID
     * @return 查询结果
     */
    UserQuerySimpleResult queryById(Long id);

    /**
     * 创建用户
     *
     * @param param    创建参数
     * @param fromSync 同步标记
     */
    void create(UserCreateParam param, boolean fromSync);

    /**
     * 修改用户
     *
     * @param param    修改参数
     * @param fromSync 同步标记
     */
    void modify(UserModifyParam param, boolean fromSync);

    /**
     * 删除用户
     *
     * @param param    删除参数
     * @param fromSync 同步标记
     */
    void delete(UserDeleteParam param, boolean fromSync);
}
