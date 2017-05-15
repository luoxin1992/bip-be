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
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
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

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<CounterQueryResult>> query(@RequestBody @Valid CounterQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<CounterQueryResult> result = counterService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @Valid CounterCreateParam param) {
        counterService.create(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid CounterModifyParam param) {
        counterService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid CounterDeleteParam param) {
        counterService.delete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
