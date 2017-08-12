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

import java.util.Optional;

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
     * 根据ID查询
     *
     * @param id 用户ID
     * @return 查询结果
     */
    UserQueryResult queryById(Long id);

    /**
     * 根据编号查询
     *
     * @param number 用户编号
     * @return 查询结果
     */
    Optional<UserQueryResult> queryByNumber(String number);

    /**
     * 创建用户
     *
     * @param param 创建参数
     */
    void create(UserCreateParam param);

    /**
     * 修改用户
     *
     * @param param 修改参数
     */
    void modify(UserModifyParam param);

    /**
     * 删除用户
     *
     * @param param 删除参数
     */
    void delete(UserDeleteParam param);
}
