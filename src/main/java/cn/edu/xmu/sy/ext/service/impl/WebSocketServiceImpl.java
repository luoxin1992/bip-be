/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.WebSocketService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luoxin
 * @version 2017-7-13
 */
@CatTransaction
@Service
public class WebSocketServiceImpl extends TextWebSocketHandler implements WebSocketService {
    private final Logger logger = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    @Autowired
    private SessionService sessionService;
    @Autowired
    private MessageService messageService;

    private Map<String, WebSocketSession> sessions;

    @PostConstruct
    private void initialize() {
        sessions = new ConcurrentHashMap<>();
    }

    @PreDestroy
    private void destroy() {
        sessions.forEach((token, session) -> {
            try {
                session.close(CloseStatus.SERVICE_RESTARTED);
                logger.info("close web socket connection {}", session.getRemoteAddress());
            } catch (IOException e) {
                logger.error("close web socket connection {} failed", session.getRemoteAddress(), e);
            }
        });
        sessions.clear();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //TODO OnOpen回调抛出异常，连接是否会被拒绝
        String token = getToken(session);

        sessionService.verify(token);
        sessions.put(token, session);
        logger.info("establish web socket connection {} with token {}", session.getRemoteAddress(), token);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String token = getToken(session);
        String body = message.getPayload();

        sessionService.verify(token);
        messageService.receive(token, body, DateTimeUtil.getNow());
        logger.info("receive {} byte(s) message from web socket connection {}",
                message.getPayloadLength(), session.getRemoteAddress());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //发生通信错误时关闭连接，正常情况下客户端会尽快重连
        session.close(CloseStatus.SERVER_ERROR);
        logger.info("web socket connection {} encounter a transport error", session.getRemoteAddress(), exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //连接已关闭，从Map中移除
        sessions.remove(getToken(session));
        logger.info("web socket connection {} close because of {}", session.getRemoteAddress(), status.getReason());
    }

    @Override
    public void sendMessage(String token, String body) {
        WebSocketSession session = sessions.get(token);
        if (session == null) {
            logger.error("web socket connection with token {} not exist", token);
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_NOT_EXIST, token);
        }

        try {
            TextMessage message = new TextMessage(body);
            session.sendMessage(message);
            logger.info("send {} byte(s) message to web socket connection {}",
                    message.getPayloadLength(), session.getRemoteAddress());
        } catch (IOException e) {
            logger.error("send message to web socket connection {} failed", session.getRemoteAddress(), e);
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_IO_ERROR);
        }
    }

    @Override
    public void closeSession(String token) {
        WebSocketSession session = sessions.get(token);
        if (session == null) {
            logger.error("web socket connection {} not exist", token);
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_NOT_EXIST, token);
        }

        try {
            session.close(CloseStatus.NORMAL);
            logger.info("close web socket connection {}", session.getRemoteAddress());
        } catch (IOException e) {
            logger.error("close web socket connection {} failed", session.getRemoteAddress(), e);
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_IO_ERROR, token);
        }
    }

    /**
     * 从(WebSocket)Session中获取Token
     *
     * @param session WebSocket Session
     * @return Token
     */
    private String getToken(WebSocketSession session) {
        //定义连接WebSocket时的URL格式：ws|wss://{host}:{port}/{endpoint}?{token}
        String token = session.getUri().getQuery();
        if (StringUtils.isEmpty(token)) {
            logger.error("token for session {} not exist", session.getRemoteAddress());
            throw new BizException(BizResultEnum.WEB_SOCKET_TOKEN_NOT_EXIST);
        }
        return token;
    }
}
