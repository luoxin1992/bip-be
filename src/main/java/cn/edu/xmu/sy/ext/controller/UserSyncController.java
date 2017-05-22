/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.edu.xmu.sy.ext.param.UserSyncCreateParam;
import cn.edu.xmu.sy.ext.param.UserSyncDeleteParam;
import cn.edu.xmu.sy.ext.param.UserSyncModifyParam;
import cn.edu.xmu.sy.ext.service.UserSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户(Push)同步Controller
 *
 * @author luoxin
 * @version 2017-3-23
 */
@RestController
@RequestMapping("/api/v1/user/sync")
public class UserSyncController {
    @Autowired
    private UserSyncService userSyncService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public BaseResponse create(@RequestBody @Valid UserSyncCreateParam param) {
        userSyncService.create(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public BaseResponse modify(@RequestBody @Valid UserSyncModifyParam param) {
        userSyncService.modify(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delete(@RequestBody @Valid UserSyncDeleteParam param) {
        userSyncService.delete(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
