/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterDeleteParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.param.CounterQueryBindParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;
import cn.edu.xmu.sy.ext.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 窗口Controller
 *
 * @author luoxin
 * @version 2017-4-23
 */
@RequestMapping("/api/v1/counter")
@RestController
public class CounterController {
    @Autowired
    private CounterService counterService;

    /**
     * @apiDefine counter 窗口API
     */
    /**
     * @api {POST} /api/v1/counter/query 查询窗口
     * @apiName query
     * @apiGroup counter
     * @apiVersion 1.0.0
     *
     * @apiParam {Object} search 搜索参数
     * @apiParam {String} [search.keyword] 关键词(编号/名称/IP地址)
     * @apiParam {Object} paging 分页参数
     * @apiParam {Number} [paging.now] 当前页码
     * @apiParam {Number} paging.size 分页长度
     * @apiParam {Number} [paging.start] 起始记录
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array} result.page 窗口(分页)
     * @apiSuccess {Number} result.page.id 窗口ID
     * @apiSuccess {String} result.page.number 编号
     * @apiSuccess {String} result.page.name 名称
     * @apiSuccess {String} result.page.mac MAC地址
     * @apiSuccess {String} result.page.ip IP地址
     * @apiSuccess {Array} result.page.sessions 会话(仅当天)
     * @apiSuccess {Number} result.page.sessions.id 会话ID
     * @apiSuccess {String} result.page.sessions.token Token
     * @apiSuccess {String} result.page.sessions.status 会话状态(在线/离线/失联)
     * @apiSuccess {String} result.page.sessions.onlineTime 上线时间
     * @apiSuccess {String} result.page.sessions.offlineTime 下线时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<CounterQueryResult>> query(@RequestBody @Valid CounterQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<CounterQueryResult> result = counterService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/query/simple", method = RequestMethod.POST)
    public BaseResponse<CounterQuerySimpleResult> querySimple(@RequestBody @Valid CounterQueryBindParam param) {
        CounterQuerySimpleResult result = counterService.querySimple(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/counter/create 添加窗口
     * @apiName create
     * @apiGroup counter
     * @apiVersion 1.0.0
     *
     * @apiParam {String} number 编号
     * @apiParam {String} name 名称
     * @apiParam {String} mac MAC地址
     * @apiParam {String} ip IP地址
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @Valid CounterCreateParam param) {
        counterService.create(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/counter/modify 编辑窗口
     * @apiName modify
     * @apiGroup counter
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 窗口ID
     * @apiParam {String} number 编号
     * @apiParam {String} name 名称
     * @apiParam {String} mac MAC地址
     * @apiParam {String} ip IP地址
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid CounterModifyParam param) {
        counterService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/counter/delete 删除窗口
     * @apiName delete
     * @apiGroup counter
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 窗口ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid CounterDeleteParam param) {
        counterService.delete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
