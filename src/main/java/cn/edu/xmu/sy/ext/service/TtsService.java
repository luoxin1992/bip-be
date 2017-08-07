/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TTS(文本到语音/语音合成)Service
 * By阿里云数加NLS(智能语音交互)
 *
 * @author luoxin
 * @version 2017-4-21
 */
public interface TtsService {
    /**
     * 异步合成
     *
     * @param content  合成内容
     * @param override 覆盖队列中的相同任务
     * @return 获取合成文件名future
     */
    CompletableFuture<String> ttsAsync(String content, boolean override);

    /**
     * 同步合成
     *
     * @param content  合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件名
     */
    String ttsSync(String content, boolean override);

    /**
     * 异步批量合成
     *
     * @param contents 合成内容
     * @param override 覆盖队列中的相同任务
     * @return 获取合成文件名future
     */
    List<CompletableFuture<String>> ttsBatchAsync(List<String> contents, boolean override);

    /**
     * 同步批量合成
     *
     * @param contents 合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件名
     */
    List<String> ttsBatchSync(List<String> contents, boolean override);
}
