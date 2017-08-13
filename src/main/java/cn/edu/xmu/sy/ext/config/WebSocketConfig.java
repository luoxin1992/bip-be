/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import cn.com.lx1992.lib.constant.CommonConstant;
import cn.edu.xmu.sy.ext.disconf.DebugConfigItem;
import cn.edu.xmu.sy.ext.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置
 *
 * @author luoxin
 * @version 2017-7-13
 */
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    private final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private DebugConfigItem debugConfig;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketHandlerRegistration registration =
                registry.addHandler((TextWebSocketHandler) webSocketService, "/client");
        logger.info("register web socket handler {}", registration);

        if (CommonConstant.TRUE.equals(debugConfig.getCrossDomainEnable())) {
            registration.setAllowedOrigins(CommonConstant.ASTERISK_STRING);
            logger.warn("allow web socket endpoint to be accessed cross domain");
        }
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
