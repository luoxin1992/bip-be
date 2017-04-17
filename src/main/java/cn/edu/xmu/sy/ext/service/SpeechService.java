/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import java.util.List;
import java.util.function.Consumer;

/**
 * 语音Service
 * <p>
 * 语音合成(By科大讯飞API)
 *
 * @author luoxin
 * @version 2017-3-27
 */
public interface SpeechService {
    /**
     * 合成语音
     * 异步方法，返回时只代表合成任务已提交
     *
     * @param content  合成内容
     * @param callback 合成回调
     */
    void synthesize(String content, Consumer<String> callback);

    /**
     * 批量合成语音
     * 异步方法，返回时只代表合成任务已提交
     *
     * @param contents 批量合成内容
     * @param callback 合成回调
     */
    void synthesizeBatch(List<String> contents, Consumer<String> callback);
}
