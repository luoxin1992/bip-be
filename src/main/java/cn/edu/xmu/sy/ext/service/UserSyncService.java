/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.UserSyncCreateParam;
import cn.edu.xmu.sy.ext.param.UserSyncDeleteParam;
import cn.edu.xmu.sy.ext.param.UserSyncModifyParam;

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
     * @param param 同步参数
     */
    void create(UserSyncCreateParam param);

    /**
     * 同步修改的用户
     *
     * @param param 同步参数
     */
    void modify(UserSyncModifyParam param);

    /**
     * 同步删除的用户
     *
     * @param param 同步参数
     */
    void delete(UserSyncDeleteParam param);
}
