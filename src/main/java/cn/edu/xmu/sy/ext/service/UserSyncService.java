/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.UserSyncParam;

/**
 * 用户(Push)同步Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface UserSyncService {
    /**
     * 同步创建的用户
     *
     * @param param 用户信息
     */
    void create(UserSyncParam param);

    /**
     * 同步修改的用户
     *
     * @param param 用户信息
     */
    void modify(UserSyncParam param);

    /**
     * 同步删除的用户
     *
     * @param param 用户信息
     */
    void delete(UserSyncParam param);
}
