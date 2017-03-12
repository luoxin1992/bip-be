/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.meta.BaseResultEnum;
import cn.com.lx1992.lib.result.BaseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luoxin
 * @version 2017-3-10
 */
@RestController
@RequestMapping("/api/v1/monitor")
public class MonitorController {
    @RequestMapping("/alive")
    public BaseResult<Void> alive() {
        return new BaseResult<>(BaseResultEnum.OK);
    }
}
