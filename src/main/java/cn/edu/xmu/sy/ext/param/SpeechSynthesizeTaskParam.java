/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import java.util.function.Consumer;

/**
 * 语音合成任务参数
 *
 * @author luoxin
 * @version 2017-4-17
 */
public class SpeechSynthesizeTaskParam extends BaseParam {
    /**
     * 合成内容
     */
    private String content;
    /**
     * 输出路径
     */
    private String path;
    /**
     * 任务超时
     */
    private int timeout;
    /**
     * 回调接口
     */
    private Consumer<String> callback;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Consumer<String> getCallback() {
        return callback;
    }

    public void setCallback(Consumer<String> callback) {
        this.callback = callback;
    }
}
