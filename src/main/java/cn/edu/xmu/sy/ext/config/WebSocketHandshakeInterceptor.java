/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import cn.edu.xmu.sy.ext.service.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket连接握手拦截器
 * <p>
 * 用于验证Token
 *
 * @author luoxin
 * @version 2017-8-15
 */
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(WebSocketHandshakeInterceptor.class);

    @Autowired
    private SessionService sessionService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //定义连接WebSocket时的URL格式：ws|wss://{host}:{port}/{endpoint}?{token}
        String token = request.getURI().getQuery();
        return !StringUtils.isEmpty(token) && sessionService.verify(token);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}
