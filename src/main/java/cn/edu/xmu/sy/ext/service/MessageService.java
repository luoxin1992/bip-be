/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceCancelParam;
import cn.edu.xmu.sy.ext.param.MessageSendServicePauseParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceResumeParam;
import cn.edu.xmu.sy.ext.param.MessageSendUpdateUserInfoParam;
import cn.edu.xmu.sy.ext.result.MessageListTypeResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;

import java.time.LocalDateTime;

/**
 * 消息Service
 *
 * @author luoxin
 * @version 2017-4-25
 */
public interface MessageService {
    /**
     * 查询全部消息类型
     *
     * @return 查询结果
     */
    BaseListResult<MessageListTypeResult> listType();

    /**
     * 查询消息
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<MessageQueryResult> query(MessageQueryParam param);

    /**
     * 删除窗口全部消息
     * @param counterId 窗口ID
     */
    void deleteByCounter(Long counterId);

    /**
     * 发送暂停服务消息
     *
     * @param param 消息参数
     */
    void sendServicePause(MessageSendServicePauseParam param);

    /**
     * 发送恢复服务消息
     *
     * @param param 消息参数
     */
    void sendServiceResume(MessageSendServiceResumeParam param);

    /**
     * 发送取消服务消息
     *
     * @param param 消息参数
     */
    void sendServiceCancel(MessageSendServiceCancelParam param);

    /**
     * 发送一般业务消息
     *
     * @param param 消息参数
     */
    void sendGeneralBusiness(MessageSendGeneralBusinessParam param);

    /**
     * 发送一般业务成功消息
     *
     * @param param 消息参数
     */
    void sendGeneralBusinessSuccess(MessageSendGeneralBusinessSuccessParam param);

    /**
     * 发送一般业务失败消息
     *
     * @param param 消息参数
     */
    void sendGeneralBusinessFailure(MessageSendGeneralBusinessFailureParam param);

    /**
     * 发送指纹登记消息
     *
     * @param param 消息参数
     */
    void sendFingerprintEnroll(MessageSendFingerprintEnrollParam param);

    /**
     * 发送指纹辨识消息
     *
     * @param param 消息参数
     */
    void sendFingerprintIdentify(MessageSendFingerprintIdentifyParam param);

    /**
     * 发送更新公司信息消息
     * 该消息会批量发送给全部在线会话
     *
     * @param logo 公司LOGO
     * @param name 公司名称
     */
    void sendUpdateCompanyInfo(String logo, String name);

    /**
     * 发送更新窗口信息消息
     *
     * @param target 消息发送目标(接收窗口ID)
     * @param number 窗口编号
     * @param name   窗口名称
     */
    void sendUpdateCounterInfo(Long target, String number, String name);

    /**
     * 发送更新用户信息消息
     *
     * @param param 消息参数
     */
    void sendUpdateUserInfo(MessageSendUpdateUserInfoParam param);

    /**
     * 重新发送消息
     *
     * @param id 消息ID
     */
    void resend(Long id);

    /**
     * 接收回复消息
     *
     * @param token     Token
     * @param message   消息
     * @param timestamp 时间戳
     */
    void receive(String token, String message, LocalDateTime timestamp);
}
