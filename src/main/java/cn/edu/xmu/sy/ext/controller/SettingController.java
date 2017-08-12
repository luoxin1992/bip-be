/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.param.BaseListParam;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.SettingQueryParam;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingQueryResult;
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
 * @apiDefine setting 设置API
 */
@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    /**
     * @api {POST} /api/v1/setting/query 查询设置
     * @apiName query
     * @apiGroup setting
     * @apiVersion 1.0.0
     *
     * @apiParam {String} parent 设置组名称
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Array}  result.total 总记录数
     * @apiSuccess {Array}  result.list 查询结果
     * @apiSuccess {Number} result.list.id ID
     * @apiSuccess {Number} result.list.key 键
     * @apiSuccess {String} result.list.value 值
     * @apiSuccess {String} result.list.regExp 校验正则
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<SettingQueryResult>> query(@RequestBody @Valid SettingQueryParam param) {
        BaseListResult<SettingQueryResult> result = settingService.query(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/setting/save 保存设置
     * @apiName save
     * @apiGroup setting
     * @apiVersion 1.0.0
     *
     * @apiParam {Array}  list 设置
     * @apiParam {String} list.key 键
     * @apiParam {String} list.value 值
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResponse save(@RequestBody @Valid BaseListParam<SettingSaveParam> param) {
        settingService.save(param);
        return new BaseResponse<>(BaseResultEnum.OK);
    }
}
