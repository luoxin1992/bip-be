/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.FingerprintListFingerParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.param.FingerprintQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintListFingerResult;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
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
 * @apiDefine fingerprint 指纹API
 */
@RestController
@RequestMapping("/api/v1/fingerprint")
public class FingerprintController {
    @Autowired
    private FingerprintService fingerprintService;

    /**
     * @api {POST} /api/v1/fingerprint/list/finger 查询可用手指
     * @apiName list-finger
     * @apiGroup fingerprint
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} userId 用户ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Object} result.list 查询结果
     * @apiSuccess {Number} result.list.code 编号
     * @apiSuccess {String} result.list.name 名称
     */
    @RequestMapping(value = "/list/finger", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<FingerprintListFingerResult>> listFinger(@RequestBody @Valid
                                                                                            FingerprintListFingerParam param) {
        BaseListResult<FingerprintListFingerResult> result = fingerprintService.listFinger(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/fingerprint/query 查询指纹
     * @apiName query
     * @apiGroup fingerprint
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} userId 用户ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.list 查询结果
     * @apiSuccess {Number} result.list.id 指纹ID
     * @apiSuccess {String} result.list.finger 手指名称
     * @apiSuccess {String} result.list.enrollTime 登记时间
     * @apiSuccess {String} result.list.identifyTime 最后辨识时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<FingerprintQueryResult>> query(@RequestBody @Valid FingerprintQueryParam param) {
        BaseListResult<FingerprintQueryResult> result = fingerprintService.query(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/fingerprint/modify 编辑指纹
     * @apiName modify
     * @apiGroup fingerprint
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 指纹ID
     * @apiParam {String} finger 手指名称
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
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
        fingerprintService.delete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
