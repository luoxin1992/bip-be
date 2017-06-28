/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingListResult;
import cn.edu.xmu.sy.ext.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 设置Controller
 *
 * @author luoxin
 * @version 2017-4-1
 */
@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    /**
     * @apiDefine setting 设置API
     */
    /**
     * @api {POST} /api/v1/setting/list 查询全部设置
     * @apiName list
     * @apiGroup setting
     * @apiVersion 1.0.0
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Array} result.groups 设置组
     * @apiSuccess {Number} result.groups.id ID
     * @apiSuccess {String} result.groups.description 描述
     * @apiSuccess {String} result.groups.remark 备注
     * @apiSuccess {Array} result.groups.items 设置项
     * @apiSuccess {Number} result.groups.items.id ID
     * @apiSuccess {String} result.groups.items.key 键
     * @apiSuccess {String} result.groups.items.value 值
     * @apiSuccess {String} result.groups.items.regExp 校验正则
     * @apiSuccess {String} result.groups.items.description 描述
     * @apiSuccess {String} result.groups.items.remark 备注
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<SettingListResult> list() {
        SettingListResult result = settingService.list();
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/setting/save 保存设置
     * @apiName save
     * @apiGroup setting
     * @apiVersion 1.0.0
     *
     * @apiParam {Array} items 设置项
     * @apiParam {Number} items.id 设置ID
     * @apiParam {String} items.key 键
     * @apiParam {String} items.value 值
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResponse<SettingListResult> save(@RequestBody @Valid SettingSaveParam param) {
        settingService.save(param);
        return new BaseResponse<>(BaseResultEnum.OK);
    }
}
