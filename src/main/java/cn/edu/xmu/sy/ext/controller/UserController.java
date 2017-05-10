/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.util.PagingUtil;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserListSimpleResult;
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
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list/simple", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<UserListSimpleResult>> listSimple() {
        BaseListResult<UserListSimpleResult> result = userService.listSimple();
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<UserQueryResult>> query(@RequestBody @Valid UserQueryParam param) {
        PagingUtil.setStartByNow(param.getPaging());
        BasePagingResult<UserQueryResult> result = userService.query(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @Valid UserCreateParam param) {
        userService.create(param, false);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid UserModifyParam param) {
        userService.modify(param, false);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid UserDeleteParam param) {
        userService.delete(param, false);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
