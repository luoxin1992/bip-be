/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.DigestUtil;
import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.config.NlsConfigItem;
import cn.edu.xmu.sy.ext.config.ServletConfigItem;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.param.ResourceCreateParam;
import cn.edu.xmu.sy.ext.param.ResourceModifyParam;
import cn.edu.xmu.sy.ext.param.TtsTaskParam;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.TtsService;
import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsFuture;
import com.alibaba.idst.nls.event.NlsEvent;
import com.alibaba.idst.nls.event.NlsListener;
import com.alibaba.idst.nls.protocol.NlsRequest;
import com.alibaba.idst.nls.session.NlsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-4-21
 */
@Service
public class TtsServiceImpl implements TtsService {
    private final Logger logger = LoggerFactory.getLogger(TtsServiceImpl.class);

    @Autowired
    private SettingService settingService;
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ServletConfigItem servletConfigItem;
    @Autowired
    private NlsConfigItem nlsConfigItem;

    private NlsClient nlsClient;
    private ExecutorService taskExecutor;
    private Map<String, CompletableFuture<String>> taskMap;

    @PostConstruct
    public void initial() {
        nlsClient = new NlsClient();
        nlsClient.init();
        logger.info("initial nls sdk");

        //taskExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        taskExecutor = Executors.newCachedThreadPool();
        taskMap = new ConcurrentHashMap<>();
    }

    @PreDestroy
    public void destroy() {
        taskExecutor.shutdown();
        try {
            logger.info("wait for tts executor shutdown");
            if (!taskExecutor.awaitTermination(nlsConfigItem.getExecutorShutdownTimeout(), TimeUnit.SECONDS)) {
                logger.warn("shutdown timeout. some tasks may be lost");
            }
        } catch (InterruptedException ignored) {
        }
        logger.info("destroy tts executor");

        nlsClient.close();
        logger.info("destroy nls sdk");
    }

    @Override
    public CompletableFuture<String> ttsAsync(TtsTaskParam param, String content, boolean override) {
        if (param == null) {
            param = createTaskParam();
        }
        TtsTaskParam finalParam = param;
        //检查相同合成内容的任务是否已在Map中
        //若有覆盖标志，则尝试取消原任务后添加新任务；否则直接返回已有Future避免重复执行
        if (taskMap.containsKey(content)) {
            logger.warn("task {} already in the async list", content);
            if (override) {
                logger.warn("force cancel tts task {}", content);
                taskMap.remove(content).cancel(false);
            } else {
                return taskMap.get(content);
            }
        }
        //提交异步任务，Future放入Map中并返回
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> this.executeTtsTask(finalParam, content), taskExecutor)
                .whenComplete((result, exception) -> {
                    try {
                        writeBackDb(content, result, exception == null);
                    } finally {
                        taskMap.remove(content);
                    }
                });
        taskMap.put(content, future);
        return future;
    }

    @Override
    public String ttsSync(TtsTaskParam param, String content, boolean override) {
        try {
            return ttsAsync(param, content, override).get();
        } catch (InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof BizException) {
                throw (BizException) e.getCause();
            } else {
                logger.error("tts unknown error", e);
                throw new BizException(BizResultEnum.TTS_UNKNOWN_ERROR);
            }
        }
    }

    @Override
    public List<CompletableFuture<String>> ttsBatchAsync(TtsTaskParam param, List<String> contents, boolean override) {
        //批量提交任务时Param可复用
        if (param == null) {
            param = createTaskParam();
        }
        TtsTaskParam finalParam = param;
        return contents.stream()
                .map((content) -> this.ttsAsync(finalParam, content, override))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> ttsBatchSync(TtsTaskParam param, List<String> contents, boolean override) {
        return contents.stream()
                .map((content) -> this.ttsSync(param, content, override))
                .collect(Collectors.toList());
    }

    /**
     * 执行TTS任务
     * 耗时操作需在新线程中执行
     *
     * @param param   任务参数
     * @param content 合成内容
     * @return 执行结果
     */
    private String executeTtsTask(TtsTaskParam param, String content) {
        //设置NLS请求参数
        NlsRequest request = new NlsRequest();
        request.setTtsEncodeType(param.getEncodeType());
        request.setTtsSpeechRate(param.getSpeechRate());
        request.setTtsVoice(param.getVoice());
        request.setTtsVolume(param.getVolume());
        request.setTtsNus(param.getNus());
        request.setTtsPitchRate(param.getPitchRate());
        request.setTtsRequest(content, param.getSampleRate());
        //NlsRequest囊括了平台全部智能语音交互业务，先设置要使用的业务，然后才能授权，否则报错
        request.setAppKey(nlsConfigItem.getAppKey());
        request.authorize(nlsConfigItem.getAccessKeyId(), nlsConfigItem.getAccessKeySecret());

        //执行TTS任务
        String outputPath = createOutputPath(param.getEncodeType());
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            logger.info("tts start, from text {} to file {}", content, outputPath);
            NlsFuture future = nlsClient.createNlsFuture(request, new NlsListenerImpl());
            byte[] buffer;
            //read方法内部hard-coding限制至多10秒超时返回
            while ((buffer = future.read()) != null) {
                fos.write(buffer);
            }
            fos.flush();
            //1. onOperationFailed在netty的worker线程上回调，但抛出异常时State会变为FAILED
            //2. 若onMessageReceived回调后任务失败，不会再回调onOperationFailed，也需要通过State进一步判断
            if (!(NlsSession.State.FINISHED.equals(future.getSession().getState()))) {
                logger.error("tts state {} error", future.getSession().getState());
                throw new BizException(BizResultEnum.TTS_STATE_ERROR);
            }
        } catch (BizException e) {
            //避免state错误的exception再被sdk错误的exception捕获
            throw e;
        } catch (IOException e) {
            logger.error("tts i/o error", e);
            throw new BizException(BizResultEnum.TTS_IO_ERROR);
        } catch (Exception e) {
            logger.error("tts sdk error", e);
            throw new BizException(BizResultEnum.TTS_SDK_ERROR);
        }
        return outputPath;
    }

    /**
     * 从设置服务取TTS配置，构建任务参数
     *
     * @return SDK参数
     */
    private TtsTaskParam createTaskParam() {
        TtsTaskParam param = new TtsTaskParam();
        //NUS模式
        String nus = settingService.getValueByKeyOptional(SettingEnum.TTS_NUS.getKey())
                .orElse(SettingEnum.TTS_NUS.getDefaultValue());
        param.setNus(Integer.parseInt(nus));
        //语音采样率
        String sampleRate = settingService.getValueByKeyOptional(SettingEnum.TTS_SAMPLE_RATE.getKey())
                .orElse(SettingEnum.TTS_SAMPLE_RATE.getDefaultValue());
        param.setSampleRate(sampleRate);
        //语音格式类型
        String encodeType = settingService.getValueByKeyOptional(SettingEnum.TTS_ENCODE_TYPE.getKey())
                .orElse(SettingEnum.TTS_ENCODE_TYPE.getDefaultValue());
        param.setEncodeType(encodeType);
        //发音人
        String voice = settingService.getValueByKeyOptional(SettingEnum.TTS_VOICE.getKey())
                .orElse(SettingEnum.TTS_VOICE.getDefaultValue());
        param.setVoice(voice);
        //合成音量
        String volume = settingService.getValueByKeyOptional(SettingEnum.TTS_VOLUME.getKey())
                .orElse(SettingEnum.TTS_VOLUME.getDefaultValue());
        param.setVolume(Integer.parseInt(volume));
        //合成语调
        String pitchRate = settingService.getValueByKeyOptional(SettingEnum.TTS_PITCH_RATE.getKey())
                .orElse(SettingEnum.TTS_PITCH_RATE.getDefaultValue());
        param.setPitchRate(Integer.parseInt(pitchRate));
        //合成语速
        String speechRate = settingService.getValueByKeyOptional(SettingEnum.TTS_SPEECH_RATE.getKey())
                .orElse(SettingEnum.TTS_SPEECH_RATE.getDefaultValue());
        param.setSpeechRate(Integer.parseInt(speechRate));
        return param;
    }

    /**
     * 生成随机文件名，由输出目录和编码类型，拼合完整输出路径
     *
     * @param encodeType 编码类型
     * @return 完整输出路径
     */
    private String createOutputPath(String encodeType) {
        String outputDir = servletConfigItem.getDocumentRoot() + File.separatorChar;
        String filename = UUIDUtil.randomUUID() + '.' + encodeType;
        return outputDir + ResourceTypeEnum.VOICE.getType() + File.separatorChar + filename;
    }

    /**
     * 将任务执行结果写回数据库
     *
     * @param content 合成内容
     * @param path    输出路径
     * @param success 成功标记(未抛出异常)
     */
    private void writeBackDb(String content, String path, boolean success) {
        Optional<Long> id = resourceService.getIdByTypeAndName(ResourceTypeEnum.VOICE.getType(), content);
        if (id.isPresent()) {
            //原本已存在的语音：新语音合成成功：更新记录；合成失败：删除记录(下次用到时重新尝试合成)
            if (success) {
                ResourceModifyParam param = new ResourceModifyParam();
                param.setId(id.get());
                //文件名即输出路径最后一个路径分隔符后面部分
                param.setFilename(path.substring(path.lastIndexOf(File.separatorChar) + 1));
                param.setMd5(DigestUtil.getFileMD5(path));
                resourceService.modify(param);
            } else {
                resourceService.delete(id.get());
            }
        } else {
            //原本不存在的语音：新语音合成成功：新增记录
            if (success) {
                ResourceCreateParam param = new ResourceCreateParam();
                param.setType(ResourceTypeEnum.VOICE.getType());
                param.setName(content);
                param.setFilename(path.substring(path.lastIndexOf(File.separatorChar) + 1));
                param.setMd5(DigestUtil.getFileMD5(path));
                resourceService.create(param);
            }
        }
    }

    class NlsListenerImpl implements NlsListener {
        @Override
        public void onMessageReceived(NlsEvent nlsEvent) {
        }

        @Override
        public void onOperationFailed(NlsEvent nlsEvent) {
            logger.error("operation failed method callback: {}", nlsEvent.getErrorMessage());
        }

        @Override
        public void onChannelClosed(NlsEvent nlsEvent) {
        }
    }
}
