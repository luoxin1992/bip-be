/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.component;

import cn.edu.xmu.sy.ext.param.SpeechSynthesizeEngineParam;
import cn.edu.xmu.sy.ext.param.SpeechSynthesizeTaskParam;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 语音合成任务
 *
 * @author luoxin
 * @version 2017-4-8
 */
public class SpeechSynthesizeTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(SpeechSynthesizeTask.class);

    private SpeechSynthesizer synthesizer;
    private SpeechSynthesizeEngineParam engineParam;
    private SpeechSynthesizeTaskParam taskParam;

    private CountDownLatch latch;
    private boolean hasError;

    private SpeechSynthesizeTask() {
        latch = new CountDownLatch(1);
        hasError = false;
    }

    public SpeechSynthesizeTask(SpeechSynthesizer synthesizer, SpeechSynthesizeEngineParam engineParam,
                                SpeechSynthesizeTaskParam taskParam) {
        this();
        this.synthesizer = synthesizer;
        this.engineParam = engineParam;
        this.taskParam = taskParam;
    }

    public String getContent() {
        return taskParam.getContent();
    }

    public String getPath() {
        return taskParam.getPath();
    }

    public Consumer<String> getCallback() {
        return taskParam.getCallback();
    }

    boolean isSuccess() {
        return !hasError;
    }

    @Override
    public void run() {
        setEngineParameter();
        synthesizeToUri();
        waitForComplete();
    }

    private void setEngineParameter() {
        synthesizer.setParameter(SpeechConstant.VOICE_NAME, engineParam.getVoiceName());
        synthesizer.setParameter(SpeechConstant.SPEED, engineParam.getSpeed());
        synthesizer.setParameter(SpeechConstant.VOLUME, engineParam.getVolume());
        synthesizer.setParameter(SpeechConstant.PITCH, engineParam.getPitch());
    }

    /**
     * 合成到文件
     */
    private void synthesizeToUri() {
        if (Math.random() > 0.5) {
            hasError = true;
            return;
        }
        //合成到文件
        logger.info("speech synthesize start, from text {} to file {}", taskParam.getContent(), taskParam.getPath());
        synthesizer.synthesizeToUri(taskParam.getContent(), taskParam.getPath(), new SynthesizeToUriListener() {
            @Override
            public void onBufferProgress(int i) {
            }

            @Override
            public void onSynthesizeCompleted(String s, SpeechError speechError) {
                //此回调方法并非运行在线程池管理的线程中，若直接抛异常，不会被处理且锁不会释放
                if (speechError != null) {
                    hasError = true;
                    logger.error("speech synthesize sdk error code {}", speechError.getErrorCode());
                }
                latch.countDown();
            }

            @Override
            public void onEvent(int i, int i1, int i2, int i3, Object o, Object o1) {
            }
        });
    }

    /**
     * 等待合成结束
     */
    private void waitForComplete() {
        try {
            if (!latch.await(taskParam.getTimeout(), TimeUnit.SECONDS)) {
                synthesizer.stopSpeaking();
                logger.error("speech synthesize timeout");
                return;
            }
        } catch (InterruptedException ignored) {
        }
        if (hasError) {
            logger.error("speech synthesize failed");
        } else {
            logger.info("speech synthesize successful");
        }
    }
}
