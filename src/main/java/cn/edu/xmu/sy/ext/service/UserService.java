/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;

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
    BasePagingResult<UserQueryResult> list(UserQueryParam param);

    /**
     * 创建用户
     *
     * @param param 用户信息
     */
    void create(UserCreateParam param);

    /**
     * 修改用户
     *
     * @param param 用户信息
     */
    void modify(UserModifyParam param);

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    void delete(Long id);
}
