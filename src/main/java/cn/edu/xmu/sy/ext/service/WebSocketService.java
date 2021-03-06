/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket服务
 *
 * @author luoxin
 * @version 2017-7-13
 */
public interface WebSocketService {
    /**
     * 添加Session到内部容器
     *
     * @param token   token
     * @param session WS连接
     */
    void addSession(String token, WebSocketSession session);

    /**
     * 发送消息到指定Token
     *
     * @param token   token
     * @param message 消息内容
     */
    void sendMessage(String token, String message);

    /**
     * 关闭连接
     *
     * @param token token
     */
    void closeSession(String token);

    /**
     * 从内部容器移除Session
     *
     * @param token token
     */
    void removeSession(String token);
}
