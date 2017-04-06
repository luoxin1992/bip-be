/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionForceOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;

import java.util.List;
import java.util.Optional;

/**
 * 会话Service
 *
 * @author luoxin
 * @version 2017-3-24
 */
public interface SessionService {
    /**
     * 会话上线
     *
     * @param param 上线参数
     */
    void online(SessionOnlineParam param);

    /**
     * 会话离线
     *
     * @param param 离线参数
     */
    void offline(SessionOfflineParam param);

    /**
     * 会话强制离线
     *
     * @param param 强制离线参数
     */
    void forceOffline(SessionForceOfflineParam param);

    /**
     * 查询会话
     *
     * @param param 查询参数
     * @return 会话信息
     */
    List<SessionQueryResult> query(SessionQueryParam param);

    /**
     * 批量查询会话
     *
     * @param param 查询参数
     * @return 会话信息
     */
    List<SessionQueryResult> queryBatch(SessionBatchQueryParam param);

    /**
     * 查询在线会话ID
     *
     * @param counterId 柜台ID
     * @return 会话ID(可选值)
     */
    Optional<Long> getOnlineSessionIdOptional(Long counterId);
}
