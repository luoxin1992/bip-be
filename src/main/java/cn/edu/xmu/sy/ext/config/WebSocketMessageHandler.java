/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

/**
 * WebSocket消息处理器
 * <p>
 * TODO 如何禁用JavaConfig对WebSocketHandler包装的LoggingWebSocketHandlerDecorator和ExceptionWebSocketHandlerDecorator
 *
 * @author luoxin
 * @version 2017-8-15
 */
@Component
public class WebSocketMessageHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(WebSocketMessageHandler.class);

    @Autowired
    private SessionService sessionService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            webSocketService.addSession(session.getUri().getQuery(), session);
            logger.info("establish web socket connection {}", session.getRemoteAddress());
        } catch (Exception e) {
            logger.error("exception in after connection established callback", e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            sessionService.verify(session.getUri().getQuery());
            messageService.receive(session.getUri().getQuery(), message.getPayload(), DateTimeUtil.getNow());
            logger.info("receive {} byte(s) message from web socket connection {}",
                    message.getPayloadLength(), session.getRemoteAddress());
        } catch (Exception e) {
            logger.error("exception in handle text message callback", e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        try {
            //发生通信错误时关闭连接，正常情况下客户端会尽快重连
            session.close(CloseStatus.SERVER_ERROR);
            logger.info("web socket connection {} encounter a transport error", session.getRemoteAddress(), exception);
        } catch (IOException e) {
            logger.error("close web socket connection {} failed", session.getRemoteAddress(), e);
        } catch (Exception e) {
            logger.error("exception in handle transport error callback", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            //连接已关闭，从Map中移除
            webSocketService.removeSession(session.getUri().getQuery());
            logger.info("web socket connection {} close, reason {}", session.getRemoteAddress(), status.getCode());
        } catch (Exception e) {
            logger.error("exception in after connection closed callback", e);
        }
    }
}
