/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.annotation.ValidateBody;
import cn.com.lx1992.lib.annotation.ValidateRule;
import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import cn.edu.xmu.sy.ext.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用户Controller
 *
 * @author luoxin
 * @version 2017-3-20
 */
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<BasePagingResult<UserQueryResult>> list(@RequestBody @ValidateBody UserQueryParam param) {
        return new BaseResponse<>(userService.list(param));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @ValidateBody UserCreateParam param) {
        userService.create(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @ValidateBody UserModifyParam param) {
        userService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public BaseResponse delete(@PathVariable("id") @ValidateRule(comment = "ID", minVal = 1) Long id) {
        userService.delete(id);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
