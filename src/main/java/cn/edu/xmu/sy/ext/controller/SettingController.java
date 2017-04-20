/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.edu.xmu.sy.ext.param.SettingSaveParam;
import cn.edu.xmu.sy.ext.result.SettingListResult;
import cn.edu.xmu.sy.ext.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 设置Controller
 *
 * @author luoxin
 * @version 2017-4-1
 */
@RestController
@RequestMapping("/api/v1/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<SettingListResult> list() {
        SettingListResult result = settingService.list();
        return new BaseResponse<>(result);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResponse<SettingListResult> save(@RequestBody @Valid SettingSaveParam param) {
        settingService.save(param);
        return new BaseResponse<>(BaseResultEnum.OK);
    }
}
