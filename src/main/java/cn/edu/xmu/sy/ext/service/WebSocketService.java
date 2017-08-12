/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

/**
 * WebSocket服务
 *
 * @author luoxin
 * @version 2017-7-13
 */
public interface WebSocketService {
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
}
