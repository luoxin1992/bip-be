/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.controller;

import cn.com.lx1992.lib.base.meta.BaseResultEnum;
import cn.com.lx1992.lib.base.response.BaseResponse;
import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.SessionKickParam;
import cn.edu.xmu.sy.ext.param.SessionOfflineParam;
import cn.edu.xmu.sy.ext.param.SessionOnlineParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
import cn.edu.xmu.sy.ext.result.SessionOnlineResult;
import cn.edu.xmu.sy.ext.result.SessionQueryResult;
import cn.edu.xmu.sy.ext.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 会话Controller
 *
 * @author luoxin
 * @version 2017-4-27
 * @apiDefine session 会话API
 */
@RestController
@RequestMapping("/api/v1/session")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    /**
     * @api {POST} /api/v1/session/online 会话上线
     * @apiName online
     * @apiGroup session
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} counterId 窗口ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {String} result.token Token
     */
    @RequestMapping(value = "/online", method = RequestMethod.POST)
    public BaseResponse<SessionOnlineResult> online(@RequestBody @Valid SessionOnlineParam param) {
        SessionOnlineResult result = sessionService.online(param);
        return new BaseResponse<>(result);
    }

    /**
     * @api {POST} /api/v1/session/offline 会话下线
     * @apiName offline
     * @apiGroup session
     * @apiVersion 1.0.0
     *
     * @apiParam {String} token Token
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    public BaseResponse offline(@RequestBody @Valid SessionOfflineParam param) {
        sessionService.offline(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/session/kick 会话强制关闭
     * @apiName kick
     * @apiGroup session
     * @apiVersion 1.0.0
     *
     * @apiParam {Number} id 会话ID
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     */
    @RequestMapping(value = "/kick", method = RequestMethod.POST)
    public BaseResponse close(@RequestBody @Valid SessionKickParam param) {
        sessionService.kick(param);
        return new BaseResponse(BaseResultEnum.OK);
    }

    /**
     * @api {POST} /api/v1/session/query 查询会话
     * @apiName query
     * @apiGroup session
     * @apiVersion 1.0.0
     *
     * @apiParam {String} counterId 窗口ID
     * @apiParam {Number} limit 预期结果长度
     *
     * @apiSuccess {Number} code 错误代码，0-成功，其他-失败
     * @apiSuccess {String} message 提示信息
     * @apiSuccess {Object} result 具体结果
     * @apiSuccess {Number} result.total 记录总数
     * @apiSuccess {String} result.list 查询结果
     * @apiSuccess {String} result.list.id 会话ID
     * @apiSuccess {String} result.list.token Token
     * @apiSuccess {String} result.list.status 状态
     * @apiSuccess {String} result.list.onlineTime 上线时间
     * @apiSuccess {String} result.list.offlineTime 下线时间
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public BaseResponse<BaseListResult<SessionQueryResult>> query(@RequestBody @Valid SessionQueryParam param) {
        BaseListResult<SessionQueryResult> result = sessionService.query(param);
        return new BaseResponse<>(result);
    }
}
