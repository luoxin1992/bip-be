/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.util.UUIDUtil;
import cn.edu.xmu.sy.ext.disconf.NlsConfigItem;
import cn.edu.xmu.sy.ext.disconf.ServletConfigItem;
import cn.edu.xmu.sy.ext.dto.TtsTaskParamDTO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.meta.SettingEnum;
import cn.edu.xmu.sy.ext.result.ResourceQuerySimpleResult;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.SettingService;
import cn.edu.xmu.sy.ext.service.TtsService;
import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsFuture;
import com.alibaba.idst.nls.event.NlsEvent;
import com.alibaba.idst.nls.event.NlsListener;
import com.alibaba.idst.nls.protocol.NlsRequest;
import com.alibaba.idst.nls.session.NlsSession;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-4-21
 */
@CatTransaction
@Service
public class TtsServiceImpl implements TtsService {
    private final Logger logger = LoggerFactory.getLogger(TtsServiceImpl.class);

    @Autowired
    private SettingService settingService;
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ServletConfigItem servletConfig;
    @Autowired
    private NlsConfigItem nlsConfig;

    private NlsClient client;
    private ExecutorService executor;
    private Map<String, CompletableFuture<String>> queue;

    @PostConstruct
    private void initialize() {
        client = new NlsClient();
        client.init();
        logger.info("initialize nls sdk");

        queue = new ConcurrentHashMap<>();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @PreDestroy
    private void destroy() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(180, TimeUnit.SECONDS);

        client.close();
        logger.info("destroy nls sdk");
    }

    @Override
    public CompletableFuture<String> ttsAsync(String content, boolean override) {
        TtsTaskParamDTO param = createTaskParam();
        return prepareTask(param, content, override);
    }

    @Override
    public String ttsSync(String content, boolean override) {
        return ttsAsync(content, override).join();
    }

    @Override
    public List<CompletableFuture<String>> ttsBatchAsync(List<String> contents, boolean override) {
        //批量提交任务时Param可复用
        TtsTaskParamDTO param = createTaskParam();
        return contents.stream()
                .map(content -> this.prepareTask(param, content, override))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> ttsBatchSync(List<String> contents, boolean override) {
        return ttsBatchAsync(contents, override).stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    /**
     * 准备TTS任务
     *
     * @param param    任务参数
     * @param content  合成内容
     * @param override 覆盖标识
     * @return 任务future
     */
    private CompletableFuture<String> prepareTask(TtsTaskParamDTO param, String content, boolean override) {
        //查询有无可复用的任务
        CompletableFuture<String> future = checkTaskExistOrCancel(content, override);
        if (future != null) {
            return future;
        }
        //提交异步任务，Future放入队列中并返回
        future = CompletableFuture
                .supplyAsync(() -> this.executeTask(param, content), executor)
                .whenComplete((result, exception) -> {
                    queue.remove(content);
                    saveResult(content, result, exception == null);
                });
        queue.put(content, future);
        return future;
    }

    /**
     * 从设置服务取TTS配置，构建任务参数
     *
     * @return TTS任务参数
     */
    private TtsTaskParamDTO createTaskParam() {
        TtsTaskParamDTO param = new TtsTaskParamDTO();
        //NUS模式
        String nus = settingService.getValueByKeyOrDefault(SettingEnum.TTS_NUS);
        param.setNus(Integer.parseInt(nus));
        //语音采样率
        String sampleRate = settingService.getValueByKeyOrDefault(SettingEnum.TTS_SAMPLE_RATE);
        param.setSampleRate(sampleRate);
        //语音格式类型
        String encodeType = settingService.getValueByKeyOrDefault(SettingEnum.TTS_ENCODE_TYPE);
        param.setEncodeType(encodeType);
        //发音人
        String voice = settingService.getValueByKeyOrDefault(SettingEnum.TTS_VOICE);
        param.setVoice(voice);
        //合成音量
        String volume = settingService.getValueByKeyOrDefault(SettingEnum.TTS_VOLUME);
        param.setVolume(Integer.parseInt(volume));
        //合成语调
        String pitchRate = settingService.getValueByKeyOrDefault(SettingEnum.TTS_PITCH_RATE);
        param.setPitchRate(Integer.parseInt(pitchRate));
        //合成语速
        String speechRate = settingService.getValueByKeyOrDefault(SettingEnum.TTS_SPEECH_RATE);
        param.setSpeechRate(Integer.parseInt(speechRate));
        return param;
    }

    /**
     * 检查队列中是否已有相同合成内容的任务
     * 若override=true，则尝试取消原任务，再添加新任务；否则直接返回已有Future，避免重复执行
     * 实际测试被取消的任务仍会执行，但无法获取结果(CancelException)
     *
     * @param content  合成内容
     * @param override 覆盖标识
     * @return 已有Future，null表示无此任务待执行
     */
    private CompletableFuture<String> checkTaskExistOrCancel(String content, boolean override) {
        if (!queue.containsKey(content)) {
            //队列中没有此任务
            return null;
        }
        if (override) {
            queue.remove(content).cancel(false);
            logger.info("force cancel tts task {}", content);
            return null;
        } else {
            logger.info("tts task {} already in async queue", content);
            return queue.get(content);
        }
    }

    /**
     * 执行TTS任务
     *
     * @param param   任务参数
     * @param content 合成内容
     * @return 合成文件名
     */
    private String executeTask(TtsTaskParamDTO param, String content) {
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
        request.setAppKey(nlsConfig.getAppKey());
        request.authorize(nlsConfig.getAccessKeyId(), nlsConfig.getAccessKeySecret());

        //执行TTS任务
        FileOutputStream outputStream = null;
        try {
            String outputPath = buildOutputPath(param.getEncodeType());
            outputStream = new FileOutputStream(outputPath);
            logger.info("tts task start, from text {} to file {}", content, outputPath);

            NlsFuture future = client.createNlsFuture(request, new NlsListenerImpl());
            byte[] buffer;
            //read方法内部hard-coding限制至多10秒超时返回
            while ((buffer = future.read()) != null) {
                outputStream.write(buffer);
            }
            outputStream.flush();

            //1. onOperationFailed在netty的worker线程上回调，但抛出异常时State会变为FAILED
            //2. 若onMessageReceived回调后任务失败，不会再回调onOperationFailed，也需要通过State进一步判断
            if (!(NlsSession.State.FINISHED.equals(future.getSession().getState()))) {
                logger.error("tts task state {} error", future.getSession().getState());
                throw new BizException(BizResultEnum.TTS_STATE_ERROR);
            }
            return getFilename(outputPath);
        } catch (BizException e) {
            //避免state错误的exception再被sdk错误的exception捕获
            throw e;
        } catch (IOException e) {
            logger.error("tts i/o error", e);
            throw new BizException(BizResultEnum.TTS_IO_ERROR);
        } catch (Exception e) {
            logger.error("tts sdk error", e);
            throw new BizException(BizResultEnum.TTS_SDK_ERROR);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    /**
     * 生成随机文件名，由输出目录和编码类型，拼合完整输出路径
     *
     * @param encodeType 编码类型
     * @return 完整输出路径
     */
    private String buildOutputPath(String encodeType) throws IOException {
        String outputDir = servletConfig.getDocumentRoot() + File.separatorChar + ResourceTypeEnum.VOICE.getType();
        String filename = UUIDUtil.randomUUID() + '.' + encodeType;
        //创建输出目录
        FileUtils.forceMkdir(Paths.get(outputDir).toFile());
        return outputDir + File.separatorChar + filename;
    }

    /**
     * 从输出路径取文件名
     * 即最后一个路径分隔符后面部分
     *
     * @param outputPath 输出路径
     * @return 文件名
     */
    private String getFilename(String outputPath) {
        return outputPath.substring(outputPath.lastIndexOf(File.separatorChar) + 1);
    }

    /**
     * 将任务执行结果写回数据库
     *
     * @param content  合成内容
     * @param filename 输出文件名
     * @param success  成功标记(未抛出异常)
     */
    private void saveResult(String content, String filename, boolean success) {
        Optional<ResourceQuerySimpleResult> result =
                resourceService.queryByTag(ResourceTypeEnum.VOICE.getType(), content);

        if (result.isPresent()) {
            //原本已存在的语音：新语音合成成功：更新记录；合成失败：删除记录(下次使用时重新尝试合成)
            if (success) {
                resourceService.modify(result.get().getId(), filename);
            } else {
                resourceService.delete(result.get().getId());
            }
        } else {
            //原本不存在的语音：新语音合成成功：创建记录
            if (success) {
                resourceService.create(ResourceTypeEnum.VOICE.getType(), content, filename);
            }
        }
    }

    class NlsListenerImpl implements NlsListener {
        @Override
        public void onMessageReceived(NlsEvent nlsEvent) {
        }

        @Override
        public void onOperationFailed(NlsEvent nlsEvent) {
            logger.warn("operation failed method callback: {}", nlsEvent.getErrorMessage());
        }

        @Override
        public void onChannelClosed(NlsEvent nlsEvent) {
        }
    }
}
