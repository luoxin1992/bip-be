/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import cn.edu.xmu.sy.ext.result.LogTypeListResult;
import cn.edu.xmu.sy.ext.result.LogQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 日志Controller
 *
 * @author luoxin
 * @version 2017-4-23
 */
@RestController
@RequestMapping("/api/v1/log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * @apiDefine log 日志API
     */
    /**
     * @api {POST} /api/v1/log/query 查询日志
     * @apiName query
     * @apiGroup log
     * @apiVersion 1.0.0
     *
     * @apiParam {String} [type] 类型
     * @apiParam {Object} search 搜索参数
     * @apiParam {String} [search.keyword] 关键词(内容)
     * @apiParam {Object} period 时间参数
     * @apiParam {String} period.start 开始时间(yyyyMMddHHmmss)
     * @apiParam {String} period.end 结束时间(yyyyMMddHHmmss)
     * @apiParam {Object} paging 分页参数
     * @apiParam {Number} [paging.now] 当前页码
     * @apiParam {Number} paging.size 分页长度
     * @apiParam {Number} [paging.start] 起始记录
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array} result.page 日志(分页)
     * @apiSuccess {Number} result.page.id ID
     * @apiSuccess {String} result.page.timestamp 时间
     * @apiSuccess {String} result.page.category 类别
     * @apiSuccess {String} result.page.content 内容
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<LogQueryResult>> query(@RequestBody @Valid LogQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<LogQueryResult> result = logService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/list/type", method = RequestMethod.POST)
    public BaseResponse<LogTypeListResult> listType() {
        LogTypeListResult result = logService.listType();
        return new BaseResponse<>(result);
    }
}
