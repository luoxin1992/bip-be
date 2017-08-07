/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.SyncFingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.SyncUserCreateParam;
import cn.edu.xmu.sy.ext.param.SyncUserDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncUserModifyParam;

/**
 * 用户(Push)同步Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface SyncService {
    /**
     * 同步创建用户
     *
     * @param param 同步参数
     */
    void userCreate(SyncUserCreateParam param);

    /**
     * 同步修改用户
     *
     * @param param 同步参数
     */
    void userModify(SyncUserModifyParam param);

    /**
     * 同步删除用户
     *
     * @param param 同步参数
     */
    void userDelete(SyncUserDeleteParam param);

    /**
     * 同步登记指纹
     *
     * @param param 同步参数
     */
    void fingerprintEnroll(SyncFingerprintEnrollParam param);

    /**
     * 同步删除指纹
     *
     * @param param 同步参数
     */
    void fingerprintDelete(SyncFingerprintDeleteParam param);
}
