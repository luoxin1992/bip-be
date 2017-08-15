/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.service.MessageService;
import cn.edu.xmu.sy.ext.service.SessionService;
import cn.edu.xmu.sy.ext.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

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
public class WebSocketServiceImpl implements WebSocketService {
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
        sessions.forEach((token, session) -> closeSession(session, CloseStatus.SERVICE_RESTARTED));
    }

    @Override
    public void addSession(String token, WebSocketSession session) {
        if (sessions.containsKey(token)) {
            //不支持同一个token同一时间建立多个连接 关闭后建立的
            closeSession(session, CloseStatus.NOT_ACCEPTABLE);
            logger.warn("web socket connection with token {} already exist", token);
        } else {
            sessions.put(token, session);
        }
    }

    @Override
    public void sendMessage(String token, String body) {
        WebSocketSession session = getSession(token);
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
        WebSocketSession session = getSession(token);
        closeSession(session, CloseStatus.NORMAL);
    }

    @Override
    public void removeSession(String token) {
        WebSocketSession session = getSession(token);
        //仍处于打开状态的session不能被移出容器
        if (session.isOpen()) {
            logger.error("web socket connection {} is still open", session.getRemoteAddress());
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_OPEN);
        }
        sessions.remove(token);
    }

    /**
     * 从内部容器获取WebSocket连接
     *
     * @param token token
     * @return WebSocket连接
     */
    private WebSocketSession getSession(String token) {
        WebSocketSession session = sessions.get(token);
        if (session == null) {
            logger.error("web socket connection with token {} not exist", token);
            throw new BizException(BizResultEnum.WEB_SOCKET_CONNECTION_NOT_EXIST, token);
        }
        return session;
    }

    /**
     * 关闭Web Socket连接
     *
     * @param session Web Socket连接
     * @param status  关闭原因
     */
    private void closeSession(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
            logger.info("close web socket connection {} due to {}", session.getRemoteAddress(), status.getCode());
        } catch (IOException e) {
            logger.error("close web socket connection {} failed", session.getRemoteAddress(), e);
        }
    }
}
