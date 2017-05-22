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

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<LogQueryResult>> query(@RequestBody @Valid LogQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<LogQueryResult> result = logService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "list/type", method = RequestMethod.POST)
    public BaseResponse<LogTypeListResult> listType() {
        LogTypeListResult result = logService.listType();
        return new BaseResponse<>(result);
    }
}
