/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.MessageQueryParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessFailureParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceCancelParam;
import cn.edu.xmu.sy.ext.param.MessageSendServicePauseParam;
import cn.edu.xmu.sy.ext.param.MessageSendServiceResumeParam;
import cn.edu.xmu.sy.ext.param.MessageSendGeneralBusinessSuccessParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.MessageSendFingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.MessageSendUpdateUserInfoParam;
import cn.edu.xmu.sy.ext.result.MessageListTypeResult;
import cn.edu.xmu.sy.ext.result.MessageQueryResult;
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
 * @apiDefine message 消息API
 */
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * @api {POST} /api/v1/message/list/type 查询全部消息类型
     * @apiName list-type
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.list 查询结果
     * @apiSuccess {String} result.list.type 类型
     * @apiSuccess {String} result.list.description 描述
     */
    @RequestMapping(value = "/list/type", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<MessageListTypeResult>> listType() {
        BaseListResult<MessageListTypeResult> result = messageService.listType();
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
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.page 查询结果
     * @apiSuccess {Number} result.page.id 消息ID
     * @apiSuccess {Number} result.page.counter 窗口信息
     * @apiSuccess {Number} result.page.counter.id 窗口ID
     * @apiSuccess {String} result.page.counter.number 编号
     * @apiSuccess {String} result.page.counter.name 名称
     * @apiSuccess {String} result.page.counter.mac MAC地址
     * @apiSuccess {String} result.page.counter.ip IP地址
     * @apiSuccess {Number} result.page.session 会话信息
     * @apiSuccess {String} result.page.session.id 会话ID
     * @apiSuccess {String} result.page.session.token Token
     * @apiSuccess {String} result.page.session.status 状态
     * @apiSuccess {String} result.page.session.onlineTime 上线时间
     * @apiSuccess {String} result.page.session.offlineTime 下线时间
     * @apiSuccess {Number} result.page.uid UID
     * @apiSuccess {String} result.page.type 类型
     * @apiSuccess {String} result.page.body 内容
     * @apiSuccess {Number} result.page.retry 重试次数
     * @apiSuccess {String} result.page.sendTime 发送时间
     * @apiSuccess {String} result.page.ackTime 确认时间
     * @apiSuccess {Object} result.page.reply 消息回复
     * @apiSuccess {Number} result.page.reply.id 消息ID
     * @apiSuccess {String} result.page.reply.type 消息类型
     * @apiSuccess {String} result.page.reply.body 消息体
     * @apiSuccess {Number} result.page.reply.retry 重试次数
     * @apiSuccess {String} result.page.reply.receiveTime 接收时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<MessageQueryResult>> query(MessageQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<MessageQueryResult> result = messageService.query(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/message/service/pause 发送“暂停服务”消息
     * @apiName service-pause
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/service/pause", method = RequestMethod.POST)
    public BaseResponse sendServicePause(@RequestBody @Valid MessageSendServicePauseParam param) {
        messageService.sendServicePause(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/service/resume 发送“恢复服务”消息
     * @apiName service-resume
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/service/resume", method = RequestMethod.POST)
    public BaseResponse sendServiceResume(@RequestBody @Valid MessageSendServiceResumeParam param) {
        messageService.sendServiceResume(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/service/cancel 发送“取消服务”消息
     * @apiName service-cancel
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/service/cancel", method = RequestMethod.POST)
    public BaseResponse sendServiceCancel(@RequestBody @Valid MessageSendServiceCancelParam param) {
        messageService.sendServiceCancel(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/general/business 发送“一般业务”消息
     * @apiName general-business
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/general/business", method = RequestMethod.POST)
    public BaseResponse sendGeneralBusiness(@RequestBody @Valid MessageSendGeneralBusinessParam param) {
        messageService.sendGeneralBusiness(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/general/business/success 发送“一般业务成功”消息
     * @apiName general-business-success
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/general/business/success", method = RequestMethod.POST)
    public BaseResponse sendGeneralBusinessSuccess(@RequestBody @Valid MessageSendGeneralBusinessSuccessParam param) {
        messageService.sendGeneralBusinessSuccess(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/general/business/failure 发送“一般业务失败”消息
     * @apiName general-business-failure
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     * @apiParam {String} [extra] 附加信息
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/general/business/failure", method = RequestMethod.POST)
    public BaseResponse sendBusinessFailure(@RequestBody @Valid MessageSendGeneralBusinessFailureParam param) {
        messageService.sendGeneralBusinessFailure(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/message/fingerprint/enroll 发送“指纹登记”消息
     * @apiName fingerprint-enroll
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     * @apiParam {Number} user 用户(ID)
     * @apiParam {Number} finger 手指
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/fingerprint/enroll", method = RequestMethod.POST)
    public BaseResponse sendFingerprintEnroll(@RequestBody @Valid MessageSendFingerprintEnrollParam param) {
        messageService.sendFingerprintEnroll(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
    
    /**
     * @api {POST} /api/v1/message/fingerprint/identify 发送“指纹辨识”消息
     * @apiName fingerprint-identify
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/fingerprint/identify", method = RequestMethod.POST)
    public BaseResponse sendFingerprintIdentify(@RequestBody @Valid MessageSendFingerprintIdentifyParam param) {
        messageService.sendFingerprintIdentify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
    
    /**
     * @api {POST} /api/v1/message/update/user 发送“更新用户信息”消息
     * @apiName update-user
     * @apiGroup message
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} target 消息发送目标(接收窗口ID)
     * @apiParam {String} number 编号
     * @apiParam {String} name 姓名
     * @apiParam {String} photo 照片(URL)
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/update/user/info", method = RequestMethod.POST)
    public BaseResponse sendUpdateUserInfo(@RequestBody @Valid MessageSendUpdateUserInfoParam param) {
        messageService.sendUpdateUserInfo(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
