/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户Controller
 *
 * @author luoxin
 * @version 2017-3-20
 * @apiDefine user 用户API
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @api {POST} /api/v1/user/query 查询用户
     * @apiName query
     * @apiGroup user
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} search 搜索参数
     * @apiParam {String} [search.keyword] 关键词(编号/姓名)
     * @apiParam {Object} paging 分页参数
     * @apiParam {Number} [paging.now] 当前页码
     * @apiParam {Number} paging.size 分页长度
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.page 查询结果
     * @apiSuccess {Number} result.page.id 用户ID
     * @apiSuccess {String} result.page.number 编号
     * @apiSuccess {String} result.page.name 姓名
     * @apiSuccess {String} result.page.photo 照片(URL)
     * @apiSuccess {Array}  result.page.fingerprints 指纹
     * @apiSuccess {Number} result.page.fingerprints.id 指纹ID
     * @apiSuccess {String} result.page.fingerprints.finger 手指名称
     * @apiSuccess {String} result.page.fingerprints.enrollTime 登记时间
     * @apiSuccess {String} result.page.fingerprints.identifyTime 最后辨识时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<UserQueryResult>> query(@RequestBody @Valid UserQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<UserQueryResult> result = userService.query(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/user/create 创建用户
     * @apiName create
     * @apiGroup user
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 编号
     * @apiParam {String} name 姓名
     * @apiParam {String} [photo] 照片
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @Valid UserCreateParam param) {
        //同步标记，区别请求是否来自/sync/**接口
        param.setFromSync(false);
        userService.create(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/user/modify 编辑用户
     * @apiName modify
     * @apiGroup user
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 用户ID
     * @apiParam {String} number 编号
     * @apiParam {String} name 姓名
     * @apiParam {String} [photo] 照片
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid UserModifyParam param) {
        param.setFromSync(false);
        userService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/user/delete 删除用户
     * @apiName delete
     * @apiGroup user
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 用户ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid UserDeleteParam param) {
        param.setFromSync(false);
        userService.delete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
