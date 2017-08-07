/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.edu.xmu.sy.ext.param.SyncFingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncFingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.SyncUserCreateParam;
import cn.edu.xmu.sy.ext.param.SyncUserDeleteParam;
import cn.edu.xmu.sy.ext.param.SyncUserModifyParam;
import cn.edu.xmu.sy.ext.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 数据同步Controller
 *
 * @author luoxin
 * @version 2017-3-23
 * @apiDefine sync 数据同步API
 */
@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {
    @Autowired
    private SyncService syncService;

    /**
     * @api {POST} /api/v1/sync/user/create 同步创建用户
     * @apiName user-create
     * @apiGroup sync
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 编号
     * @apiParam {String} name 姓名
     * @apiParam {String} photo 照片
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public BaseResponse userCreate(@RequestBody @Valid SyncUserCreateParam param) {
        syncService.userCreate(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/sync/user/modify 同步编辑用户
     * @apiName user-modify
     * @apiGroup sync
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 编号
     * @apiParam {String} name 姓名
     * @apiParam {String} photo 照片
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/user/modify", method = RequestMethod.POST)
    public BaseResponse userModify(@RequestBody @Valid SyncUserModifyParam param) {
        syncService.userModify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/sync/user/delete 同步删除用户
     * @apiName user-delete
     * @apiGroup sync
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public BaseResponse userDelete(@RequestBody @Valid SyncUserDeleteParam param) {
        syncService.userDelete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/sync/fingerprint/enroll 同步登记指纹
     * @apiName fingerprint-enroll
     * @apiGroup sync
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 用户编号
     * @apiParam {String} template 指纹模板
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/fingerprint/enroll", method = RequestMethod.POST)
    public BaseResponse fingerprintEnroll(@RequestBody @Valid SyncFingerprintEnrollParam param) {
        syncService.fingerprintEnroll(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/sync/fingerprint/delete 同步删除指纹
     * @apiName fingerprint-delete
     * @apiGroup sync
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 用户编号
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/fingerprint/delete", method = RequestMethod.POST)
    public BaseResponse fingerprintDelete(@RequestBody @Valid SyncFingerprintDeleteParam param) {
        syncService.fingerprintDelete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}