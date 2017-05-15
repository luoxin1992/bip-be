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
import cn.edu.xmu.sy.ext.result.ResourceListSimpleResult;
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
 */
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/list/simple", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<ResourceListSimpleResult>> listSimple() {
        BaseListResult<ResourceListSimpleResult> results = resourceService.listSimple();
        return new BaseResponse<>(results);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<ResourceQueryResult>> query(@RequestBody @ValidateBody
                                                                             ResourceQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<ResourceQueryResult> result = resourceService.query(param);
        return new BaseResponse<>(result);
    }
}
