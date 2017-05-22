/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.MessageBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessPauseParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessProcessParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessResumeParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageCloseParam;
import cn.edu.xmu.sy.ext.param.MessageCounterInfoParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageReplyParam;
import cn.edu.xmu.sy.ext.param.MessageUserInfoParam;
import cn.edu.xmu.sy.ext.result.MessageFingerprintIdentifyResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;
import cn.edu.xmu.sy.ext.result.MessageTypeListResult;

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
    MessageTypeListResult listType();

    /**
     * 查询消息
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<MessageQueryResult> query(MessageQueryParam param);

    /**
     * 发送Token注册消息
     *
     * @param token Token
     */
    void sendTokenRegister(String token);

    /**
     * 发送Token解除注册消息
     *
     * @param token Token
     */
    void sendTokenUnregister(String token);

    /**
     * 发送业务暂停受理消息
     *
     * @param param 消息参数
     */
    void sendBusinessPause(MessageBusinessPauseParam param);

    /**
     * 发送业务恢复受理消息
     *
     * @param param 消息参数
     */
    void sendBusinessResume(MessageBusinessResumeParam param);

    /**
     * 发送业务正在受理消息
     *
     * @param param 消息参数
     */
    void sendBusinessProcess(MessageBusinessProcessParam param);

    /**
     * 发送业务受理成功消息
     *
     * @param param 消息参数
     */
    void sendBusinessSuccess(MessageBusinessSuccessParam param);

    /**
     * 发送业务受理失败消息
     *
     * @param param 消息参数
     */
    void sendBusinessFailure(MessageBusinessFailureParam param);

    /**
     * 发送指纹登记消息
     *
     * @param param 消息参数
     */
    void sendFingerprintEnroll(MessageFingerprintEnrollParam param);

    /**
     * 发送指纹辨识消息
     *
     * @param param 消息参数
     * @return 辨识结果(用户信息)
     */
    MessageFingerprintIdentifyResult sendFingerprintIdentify(MessageFingerprintIdentifyParam param);

    /**
     * 发送关闭客户端消息
     *
     * @param param 消息参数
     */
    void sendClose(MessageCloseParam param);

    /**
     * 发送更新窗口信息消息
     *
     * @param param 消息参数
     */
    void sendCounterInfo(MessageCounterInfoParam param);

    /**
     * 发送更新用户信息消息
     *
     * @param param 消息参数
     */
    void sendUserInfo(MessageUserInfoParam param);

    /**
     * 接收消息回复
     *
     * @param param 消息参数
     */
    void receiveReply(MessageReplyParam param);
}
