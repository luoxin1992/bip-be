/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author luoxin
 * @version 2017-4-27
 */
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/online", method = RequestMethod.POST)
    public BaseResponse<SessionOnlineResult> online(@RequestBody @Valid SessionOnlineParam param) {
        SessionOnlineResult result = sessionService.online(param);
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    public BaseResponse offline(@RequestBody @Valid SessionOfflineParam param) {
        sessionService.offline(param);
        return new BaseResponse(BaseResultEnum.OK);
    }
}
