/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.SessionKickParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.result.SessionQuerySimpleResult;

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
     * 会话强制下线
     *
     * @param param 强制离线参数
     */
    void kick(SessionKickParam param);

    /**
     * 会话失联
     *
     * @param token Token
     */
    void lost(String token);

    /**
     * 会话校验
     * “会话有效”定义为token存在且session状态为ONLINE
     *
     * @param token Token
     */
    void verify(String token);

    /**
     * 删除窗口全部会话
     *
     * @param counterId 窗口ID
     */
    void deleteByCounter(Long counterId);

    /**
     * 查询会话
     *
     * @param param 查询参数
     * @return 会话信息
     */
    BaseListResult<SessionQueryResult> query(SessionQueryParam param);

    /**
     * 查询指定窗口的在线会话
     *
     * @param counterId 窗口ID
     * @return 查询结果
     */
    Optional<SessionQuerySimpleResult> queryOnline(Long counterId);

    /**
     * 查询在线会话
     *
     * @param sessionId 会话ID
     * @return 查询结果
     */
    SessionQuerySimpleResult queryIfOnline(Long sessionId);

    /**
     * 查询全部在线会话
     * @return 查询结果
     */
    List<SessionQuerySimpleResult> queryAllOnline();

    /**
     * 批量查询，映射到会话ID
     *
     * @param sessionIds 会话ID
     * @return 查询结果
     */
    Map<Long, SessionQueryResult> queryBatch(List<Long> sessionIds);

    /**
     * 批量查询，按窗口ID分组
     *
     * @param counterIds 窗口ID
     * @return 查询结果
     */
    Map<Long, List<SessionQueryResult>> queryBatchAndGroup(List<Long> counterIds);
}
