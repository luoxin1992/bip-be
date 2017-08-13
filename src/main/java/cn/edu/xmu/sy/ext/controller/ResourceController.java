/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.annotation.ValidateBody;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListResult;
import cn.edu.xmu.sy.ext.result.ResourceListTypeResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源Controller
 *
 * @author luoxin
 * @version 2017-3-29
 * @apiDefine resource 资源API
 */
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * @api {POST} /api/v1/resource/list 查询全部资源
     * @apiName list
     * @apiGroup resource
     * @apiVersion 1.0.0
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.list 查询结果
     * @apiSuccess {Number} result.list.id 资源ID
     * @apiSuccess {String} result.list.type 类型
     * @apiSuccess {String} result.list.url URL
     * @apiSuccess {String} result.list.md5 MD5
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<ResourceListResult>> list() {
        BaseListResult<ResourceListResult> results = resourceService.list();
        return new BaseResponse<>(results);
    }

    /**
     * @api {POST} /api/v1/resource/list/type 查询资源类型
     * @apiName list-type
     * @apiGroup resource
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
    public BaseResponse<BaseListResult<ResourceListTypeResult>> listType() {
        BaseListResult<ResourceListTypeResult> results = resourceService.listType();
        return new BaseResponse<>(results);
    }



    /**
     * @api {POST} /api/v1/resource/query 查询资源
     * @apiName query
     * @apiGroup resource
     * @apiVersion 1.0.0
     *
     * @apiParam {String} [type] 类型
     * @apiParam {Object} search 搜索参数
     * @apiParam {String} [search.keyword] 关键词(标签)
     * @apiParam {Object} period 时间参数
     * @apiParam {String} period.start 开始时间(yyyyMMddHHmmss)
     * @apiParam {String} period.end 结束时间(yyyyMMddHHmmss)
     * @apiParam {Object} paging 分页参数
     * @apiParam {Number} [paging.now] 当前页码
     * @apiParam {Number} paging.size 分页长度
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 总记录数
     * @apiSuccess {Array}  result.page 查询结果
     * @apiSuccess {Number} result.page.id 资源ID
     * @apiSuccess {String} result.page.type 类型
     * @apiSuccess {String} result.page.tag 标签
     * @apiSuccess {String} result.page.url URL
     * @apiSuccess {String} result.page.timestamp 修改时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<ResourceQueryResult>> query(@RequestBody @ValidateBody
                                                                             ResourceQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<ResourceQueryResult> result = resourceService.query(param);
        return new BaseResponse<>(result);
    }
}
