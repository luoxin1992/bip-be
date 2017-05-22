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

    @RequestMapping(value = "/list/type", method = RequestMethod.POST)
    public BaseResponse<MessageTypeListResult> listType() {
        MessageTypeListResult result = messageService.listType();
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<MessageQueryResult>> query(MessageQueryParam param) {
        BasePagingResult<MessageQueryResult> result = messageService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/business/pause", method = RequestMethod.POST)
    public BaseResponse sendBusinessPause(@RequestBody @Valid MessageBusinessPauseParam param) {
        messageService.sendBusinessPause(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/business/resume", method = RequestMethod.POST)
    public BaseResponse sendBusinessResume(@RequestBody @Valid MessageBusinessResumeParam param) {
        messageService.sendBusinessResume(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/business/process", method = RequestMethod.POST)
    public BaseResponse sendBusinessProcess(@RequestBody @Valid MessageBusinessProcessParam param) {
        messageService.sendBusinessProcess(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/business/success", method = RequestMethod.POST)
    public BaseResponse sendBusinessSuccess(@RequestBody @Valid MessageBusinessSuccessParam param) {
        messageService.sendBusinessSuccess(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/business/failure", method = RequestMethod.POST)
    public BaseResponse sendBusinessFailure(@RequestBody @Valid MessageBusinessFailureParam param) {
        messageService.sendBusinessFailure(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/fingerprint/enroll", method = RequestMethod.POST)
    public BaseResponse sendFingerprintEnroll(@RequestBody @Valid MessageFingerprintEnrollParam param) {
        messageService.sendFingerprintEnroll(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/fingerprint/identify", method = RequestMethod.POST)
    public BaseResponse<MessageFingerprintIdentifyResult> sendFingerprintIdentify(
            @RequestBody @Valid MessageFingerprintIdentifyParam param) {
        MessageFingerprintIdentifyResult result = messageService.sendFingerprintIdentify(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public BaseResponse sendClose(@RequestBody @Valid MessageCloseParam param) {
        messageService.sendClose(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public BaseResponse receiveReply(@RequestBody @Valid MessageReplyParam param) {
        messageService.receiveReply(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
