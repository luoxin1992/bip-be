/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionCloseParam;
import cn.edu.xmu.sy.ext.param.SessionLostClientParam;
import cn.edu.xmu.sy.ext.param.SessionLostServerParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;

import java.util.List;
import java.util.Map;
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
     * @return 上线结果
     */
    SessionOnlineResult online(SessionOnlineParam param);

    /**
     * 会话离线
     *
     * @param param 离线参数
     */
    void offline(SessionOfflineParam param);

    /**
     * 会话服务端失联
     *
     * @param param 失联参数
     */
    void lostServer(SessionLostServerParam param);

    /**
     * 会话客户端失联
     *
     * @param param 失联参数
     */
    void lostClient(SessionLostClientParam param);

    /**
     * 会话强制关闭
     *
     * @param param 关闭参数
     */
    void close(SessionCloseParam param);

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
     * @return 查询结果
     */
    List<SessionQueryResult> queryBatch(SessionBatchQueryParam param);

    /**
     * 批量查询会话，并按窗口ID分组
     *
     * @param param 查询参数
     * @return 查询结果
     */
    Map<Long, List<SessionQueryResult>> queryBatchAndGroup(SessionBatchQueryParam param);

    /**
     * 根据窗口ID查询在线会话ID
     *
     * @param counterId 窗口ID
     * @return 会话ID(可选值)
     */
    Optional<Long> getOnlineIdByCounterIdOptional(Long counterId);

    /**
     * 查询会话Token
     *
     * @param id 会话ID
     * @return Token
     */
    String getTokenById(Long id);
}
