/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.MessageBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessPauseParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessProcessParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessResumeParam;
import cn.edu.xmu.sy.ext.param.MessageBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageCloseParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageReplyParam;
import cn.edu.xmu.sy.ext.result.MessageFingerprintIdentifyResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;
import cn.edu.xmu.sy.ext.result.MessageTypeListResult;
import cn.edu.xmu.sy.ext.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 消息Controller
 *
 * @author luoxin
 * @version 2017-4-28
 */
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * @apiDefine message 消息API
     */
    @RequestMapping(value = "/list/type", method = RequestMethod.POST)
    public BaseResponse<MessageTypeListResult> listType() {
        MessageTypeListResult result = messageService.listType();
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/message/query 查询消息
     * @apiName query
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {String} [type] 类型
     * @apiParam {Object} search 搜索参数
     * @apiParam {String} [search.keyword] 关键词(消息体)
     * @apiParam {Object} period 时间区间
     * @apiParam {String} period.start 开始时间(yyyyMMddHHmmss)
     * @apiParam {String} period.end 结束时间(yyyyMMddHHmmss)
     * @apiParam {Object} paging 分页参数
     * @apiParam {NUmber} [paging.now] 当前页码
     * @apiParam {Number} paging.size 分页长度
     * @apiParam {Number} [paging.start] 起始记录
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array} result.page 消息(分页)
     * @apiSuccess {Number} result.page.id 消息ID
     * @apiSuccess {Number} result.page.counterId 窗口ID
     * @apiSuccess {Number} result.page.sessionId 会话ID
     * @apiSuccess {String} result.page.type 类型
     * @apiSuccess {String} result.page.body 内容
     * @apiSuccess {String} result.page.sendTime 发送时间
     * @apiSuccess {String} result.page.ackTime 确认时间
     * @apiSuccess {Object} result.page.reply 消息回复
     * @apiSuccess {Number} result.page.reply.id 消息ID
     * @apiSuccess {String} result.page.reply.body 消息体
     * @apiSuccess {String} result.page.reply.sendTime 发送时间
     * @apiSuccess {String} result.page.reply.ackTime 确认时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<MessageQueryResult>> query(MessageQueryParam param) {
        BasePagingResult<MessageQueryResult> result = messageService.query(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/message/business/pause 发送“暂停服务”消息
     * @apiName business-pause
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/business/pause", method = RequestMethod.POST)
    public BaseResponse sendBusinessPause(@RequestBody @Valid MessageBusinessPauseParam param) {
        messageService.sendBusinessPause(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/business/resume 发送“恢复服务”消息
     * @apiName business-resume
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/business/resume", method = RequestMethod.POST)
    public BaseResponse sendBusinessResume(@RequestBody @Valid MessageBusinessResumeParam param) {
        messageService.sendBusinessResume(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/business/process 发送“业务正在受理”消息
     * @apiName business-process
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/business/process", method = RequestMethod.POST)
    public BaseResponse sendBusinessProcess(@RequestBody @Valid MessageBusinessProcessParam param) {
        messageService.sendBusinessProcess(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/business/success 发送“业务受理成功”消息
     * @apiName business-success
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/business/success", method = RequestMethod.POST)
    public BaseResponse sendBusinessSuccess(@RequestBody @Valid MessageBusinessSuccessParam param) {
        messageService.sendBusinessSuccess(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/business/failure 发送“业务受理失败”消息
     * @apiName business-failure
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/business/failure", method = RequestMethod.POST)
    public BaseResponse sendBusinessFailure(@RequestBody @Valid MessageBusinessFailureParam param) {
        messageService.sendBusinessFailure(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/fingerprint/enroll 发送“指纹登记”消息
     * @apiName fingerprint-enroll
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     * @apiParam {Number} [id] 用户ID
     * @apiParam {String} [number] 用户编号
     * @apiParam {String} finger 手指名称
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/fingerprint/enroll", method = RequestMethod.POST)
    public BaseResponse sendFingerprintEnroll(@RequestBody @Valid MessageFingerprintEnrollParam param) {
        messageService.sendFingerprintEnroll(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/fingerprint/identify 发送“指纹辨识”消息
     * @apiName fingerprint-identify
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {String} result.number 编号
     * @apiSuccess {String} result.name 姓名
     * @apiSuccess {String} result.photo 照片(URL)
     */
    @RequestMapping(value = "/fingerprint/identify", method = RequestMethod.POST)
    public BaseResponse<MessageFingerprintIdentifyResult> sendFingerprintIdentify(
            @RequestBody @Valid MessageFingerprintIdentifyParam param) {
        MessageFingerprintIdentifyResult result = messageService.sendFingerprintIdentify(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/message/close 发送“关闭客户端”消息
     * @apiName close
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} sendTo 消息接收目标
     * @apiParam {Number} [sendTo.id] 窗口ID
     * @apiParam {String} [sendTo.number] 窗口编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public BaseResponse sendClose(@RequestBody @Valid MessageCloseParam param) {
        messageService.sendClose(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/reply 接收消息回复
     * @apiName reply
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 消息ID
     * @apiParam {String} body 消息体
     * @apiParam {String} timestamp 时间戳(yyyyMMddHHmmss)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public BaseResponse receiveReply(@RequestBody @Valid MessageReplyParam param) {
        messageService.receiveReply(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
