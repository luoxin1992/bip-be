/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.dto.DiffFieldDTO;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import cn.edu.xmu.sy.ext.result.LogQueryResult;
import cn.edu.xmu.sy.ext.result.LogTypeListResult;

import java.util.List;

/**
 * 日志Service
 *
 * @author luoxin
 * @version 2017-3-24
 */
public interface LogService {
    /**
     * 查询全部日志类型
     *
     * @return 查询结果
     */
    LogTypeListResult listType();

    /**
     * 查询日志
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<LogQueryResult> query(LogQueryParam param);

    /**
     * 记录“新增用户”日志
     *
     * @param userId 用户ID
     * @param number 用户编号
     * @param name   用户姓名
     */
    void logUserCreate(Long userId, String number, String name);

    /**
     * 记录“修改用户”日志
     *
     * @param userId     用户ID
     * @param diffFields 修改的字段
     */
    void logUserModify(Long userId, List<DiffFieldDTO> diffFields);

    /**
     * 记录“删除用户”日志
     *
     * @param userId 用户ID
     */
    void logUserDelete(Long userId);

    /**
     * 记录“修改指纹”日志
     *
     * @param fingerprintId 指纹ID
     * @param diffFields    修改的字段
     */
    void logFingerprintModify(Long fingerprintId, List<DiffFieldDTO> diffFields);

    /**
     * 记录“删除指纹”日志
     *
     * @param fingerprintId 指纹ID
     */
    void logFingerprintDelete(Long fingerprintId);

    /**
     * 记录“删除用户全部指纹”日志
     *
     * @param userId 用户ID
     */
    void logFingerprintDeleteByUser(Long userId);

    /**
     * 记录“登记指纹”日志
     *
     * @param fingerprintId  指纹ID
     * @param userId         用户ID
     * @param fingerprintUid 指纹UID
     */
    void logFingerprintEnroll(Long fingerprintId, Long userId, Integer fingerprintUid);

    /**
     * 记录“辨识指纹”日志
     *
     * @param fingerprintId  指纹ID
     * @param userId         用户ID
     * @param fingerprintUid 指纹UID
     */
    void logFingerprintIdentify(Long fingerprintId, Long userId, Integer fingerprintUid);

    /**
     * 记录“新增窗口”日志
     *
     * @param counterId 窗口ID
     * @param number    窗口编号
     * @param name      窗口名称
     */
    void logCounterCreate(Long counterId, String number, String name);

    /**
     * 记录“修改窗口”日志
     *
     * @param counterId  窗口ID
     * @param diffFields 修改的字段
     */
    void logCounterModify(Long counterId, List<DiffFieldDTO> diffFields);

    /**
     * 记录“删除窗口”日志
     *
     * @param counterId 窗口ID
     */
    void logCounterDelete(Long counterId);

    /**
     * 记录“新增会话”日志
     *
     * @param sessionId 会话ID
     * @param counterId 窗口ID
     * @param token     会话Token
     */
    void logSessionCreate(Long sessionId, Long counterId, String token);

    /**
     * 记录“修改会话状态”日志
     *
     * @param sessionId 会话ID
     * @param token     会话Token
     * @param status    会话状态
     */
    void logSessionUpdateStatus(Long sessionId, String token, String status);

    /**
     * 记录“删除会话”日志
     *
     * @param counterId 窗口ID
     */
    void logSessionDeleteByCounter(Long counterId);

    /**
     * 记录“发送消息”日志
     *
     * @param messageId     消息ID
     * @param counterId     窗口ID
     * @param sessionId     会话ID
     * @param messageUid    消息UID
     * @param messageType   消息类型
     * @param messageLength 消息长度
     */
    void logMessageSend(Long messageId, Long counterId, Long sessionId, Long messageUid, MessageTypeEnum messageType,
                        Integer messageLength);

    /**
     * 记录“接收消息”日志
     *
     * @param messageId     消息ID
     * @param messageUid    消息UID
     * @param messageType   消息类型
     * @param messageLength 消息长度
     */
    void logMessageReceive(Long messageId, Long messageUid, String messageType, Integer messageLength);

    /**
     * 记录“删除消息”日志
     *
     * @param counterId 窗口ID
     */
    void logMessageDeleteByCounter(Long counterId);

    /**
     * 记录“保存设置”日志
     *
     * @param description 设置描述
     * @param from        设置原值
     * @param to          设置新值
     */
    void logSettingSave(String description, String from, String to);

    /**
     * 记录“新增资源”日志
     *
     * @param resourceId 资源ID
     * @param tag        资源标签
     * @param filename   资源文件名
     */
    void logResourceCreate(Long resourceId, String tag, String filename);

    /**
     * 记录“修改资源”日志
     *
     * @param resourceId 资源ID
     * @param diffFields 修改的字段
     */
    void logResourceModify(Long resourceId, List<DiffFieldDTO> diffFields);

    /**
     * 记录“删除资源”日志
     *
     * @param resourceId 资源ID
     */
    void logResourceDelete(Long resourceId);
}
