/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteByUserParam;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.FingerprintFingerListParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.result.FingerprintFingerListResult;
import cn.edu.xmu.sy.ext.result.FingerprintTemplateListResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 指纹Controller
 *
 * @author luoxin
 * @version 2017-5-9
 */
@RestController
@RequestMapping("/api/v1/fingerprint")
public class FingerprintController {
    @Autowired
    private FingerprintService fingerprintService;

    /**
     * @apiDefine fingerprint 指纹API
     */
    @RequestMapping(value = "/list/finger", method = RequestMethod.POST)
    public BaseResponse<FingerprintFingerListResult> listFinger() {
        FingerprintFingerListResult result = fingerprintService.listFinger();
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/list/finger/usable", method = RequestMethod.POST)
    public BaseResponse<FingerprintFingerListResult> listFingerUsable(@RequestBody @Valid
                                                                              FingerprintFingerListParam param) {
        FingerprintFingerListResult result = fingerprintService.listFingerUsable(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/list/template", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<FingerprintTemplateListResult>> listTemplate() {
        BaseListResult<FingerprintTemplateListResult> result = fingerprintService.listTemplate();
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid FingerprintModifyParam param) {
        fingerprintService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/fingerprint/delete 删除指纹
     * @apiName delete
     * @apiGroup fingerprint
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 指纹ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid FingerprintDeleteParam param) {
        fingerprintService.delete(param.getId());
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/delete/user", method = RequestMethod.POST)
    public BaseResponse deleteByUser(@RequestBody @Valid FingerprintDeleteByUserParam param) {
        fingerprintService.deleteByUser(param.getUserId());
        return new BaseResponse(BaseResultEnum.OK);
    }
}
