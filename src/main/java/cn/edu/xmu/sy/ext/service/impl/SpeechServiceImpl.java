/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.component.SpeechSynthesizeExecutor;
import cn.edu.xmu.sy.ext.component.SpeechSynthesizeTask;
import cn.edu.xmu.sy.ext.param.SpeechSynthesizeEngineParam;
import cn.edu.xmu.sy.ext.param.SpeechSynthesizeTaskParam;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.SpeechService;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author luoxin
 * @version 2017-3-27
 */
@Service
public class SpeechServiceImpl implements SpeechService {
    private final Logger logger = LoggerFactory.getLogger(SpeechServiceImpl.class);

    @Autowired
    private SettingService settingService;

    @Autowired
    private SpeechSynthesizeExecutor executor;

    private SpeechSynthesizer synthesizer;

    @PostConstruct
    public void initial() {
        //TODO 配置中心取APPID
        SpeechUtility.createUtility(SpeechConstant.APPID + "=" + "58b6b475");
        synthesizer = SpeechSynthesizer.createSynthesizer();
        logger.info("iflytek sdk initial");
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
        try {
            logger.info("wait for speech synthesize executor shutdown");
            //TODO 超时可配
            if (!executor.awaitTermination(180, TimeUnit.SECONDS)) {
                logger.warn("shutdown timeout, some tasks may lost");
            }
        } catch (InterruptedException ignored) {
        }
        logger.info("speech synthesize executor destroy");

        synthesizer.destroy();
        logger.info("iflytek sdk destroy");
    }

    @Override
    public synchronized void synthesize(String content, Consumer<String> callback) {
        SpeechSynthesizeEngineParam engineParam = getParam();
        SpeechSynthesizeTaskParam taskParam = new SpeechSynthesizeTaskParam();
        taskParam.setContent(content);
        taskParam.setPath(getPath());
        //TODO 超时可配
        taskParam.setTimeout(5);
        taskParam.setCallback(callback);
        SpeechSynthesizeTask task = new SpeechSynthesizeTask(synthesizer, engineParam, taskParam);
        executor.execute(task);
    }

    @Override
    public synchronized void synthesizeBatch(List<String> contents, Consumer<String> callback) {
        SpeechSynthesizeEngineParam engineParam = getParam();
        contents.forEach((content) -> {
            SpeechSynthesizeTaskParam taskParam = new SpeechSynthesizeTaskParam();
            taskParam.setContent(content);
            taskParam.setPath(getPath());
            //TODO 超时可配
            taskParam.setTimeout(5);
            taskParam.setCallback(callback);
            SpeechSynthesizeTask task = new SpeechSynthesizeTask(synthesizer, engineParam, taskParam);
            executor.execute(task);
        });
    }

    /**
     * 从设置服务取合成参数
     */
    private SpeechSynthesizeEngineParam getParam() {
        SpeechSynthesizeEngineParam param = new SpeechSynthesizeEngineParam();
        //发音人（小燕/小宇/小研/小琪/小峰）
        String voiceName = settingService.getValueByKeyOptional(SettingEnum.SPEECH_VOICE_NAME.getKey())
                .orElse(SettingEnum.SPEECH_VOICE_NAME.getValue());
        param.setVolume(voiceName);
        //合成语速
        String speed = settingService.getValueByKeyOptional(SettingEnum.SPEECH_SPEED.getKey())
                .orElse(SettingEnum.SPEECH_SPEED.getValue());
        param.setSpeed(speed);
        //合成音量
        String volume = settingService.getValueByKeyOptional(SettingEnum.SPEECH_VOLUME.getKey())
                .orElse(SettingEnum.SPEECH_VOLUME.getValue());
        param.setVolume(volume);
        //合成语调
        String pitch = settingService.getValueByKeyOptional(SettingEnum.SPEECH_PITCH.getKey())
                .orElse(SettingEnum.SPEECH_PITCH.getValue());
        param.setPitch(pitch);
        return param;
    }

    /**
     * 从设置服务取输出目录，生成随机文件名，拼合完整输出路径
     *
     * @return 输出路径
     */
    private String getPath() {
        String outputDir = settingService.getValueByKeyOptional(SettingEnum.SPEECH_OUTPUT_DIR.getKey())
                .orElse(SettingEnum.SPEECH_OUTPUT_DIR.getValue());
        String filename = UUIDUtil.randomUUID();
        return outputDir + File.separatorChar + filename;
    }
}
