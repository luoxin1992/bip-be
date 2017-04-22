/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.TtsTaskParam;

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
     * @param param    任务参数(可选)
     * @param content  合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件路径
     */
    CompletableFuture<String> ttsAsync(TtsTaskParam param, String content, boolean override);

    /**
     * 同步合成
     *
     * @param param    任务参数(可选)
     * @param content  合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件路径
     */
    String ttsSync(TtsTaskParam param, String content, boolean override);

    /**
     * 异步批量合成
     *
     * @param param    任务参数(可选)
     * @param contents 合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件路径
     */
    List<CompletableFuture<String>> ttsBatchAsync(TtsTaskParam param, List<String> contents, boolean override);

    /**
     * 同步批量合成
     *
     * @param param    任务参数(可选)
     * @param contents 合成内容
     * @param override 覆盖队列中的相同任务
     * @return 合成文件路径
     */
    List<String> ttsBatchSync(TtsTaskParam param, List<String> contents, boolean override);
}
