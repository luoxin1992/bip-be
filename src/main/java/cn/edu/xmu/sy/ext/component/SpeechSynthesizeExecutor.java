/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.component;

import cn.com.lx1992.lib.util.DigestUtil;
import cn.edu.xmu.sy.ext.param.ResourceCreateParam;
import cn.edu.xmu.sy.ext.param.ResourceModifyParam;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 语音合成任务执行队列
 * 采用基于链式阻塞队列的单一线程池模型
 *
 * @author luoxin
 * @version 2017-4-8
 */
@Component
public class SpeechSynthesizeExecutor extends ThreadPoolExecutor {
    private final Logger logger = LoggerFactory.getLogger(SpeechSynthesizeExecutor.class);

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final int KEEP_ALIVE_TIME = 0;

    @Autowired
    private ResourceService resourceService;

    public SpeechSynthesizeExecutor() {
        super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        SpeechSynthesizeTask task = (SpeechSynthesizeTask) r;
        //将合成的语音信息保存到数据库
        Optional<Long> id = resourceService.getIdByTypeAndName(ResourceTypeEnum.VOICE.getType(), task.getContent());
        if (id.isPresent()) {
            //原本已存在的语音：新语音合成成功：更新记录，合成失败：删除记录(下次用到时重新尝试合成)
            if (task.isSuccess()) {
                ResourceModifyParam param = new ResourceModifyParam();
                param.setId(id.get());
                param.setPath(task.getPath());
                param.setMd5(DigestUtil.getFileMD5(task.getPath()));
                resourceService.modify(param);
            } else {
                resourceService.delete(id.get());
            }
        } else {
            //原本不存在的语音：新语音合成成功：新增记录
            if (task.isSuccess()) {
                ResourceCreateParam param = new ResourceCreateParam();
                param.setType(ResourceTypeEnum.VOICE.getType());
                param.setName(task.getContent());
                param.setPath(task.getPath());
                param.setMd5(DigestUtil.getFileMD5(task.getPath()));
                resourceService.create(param);
            }
        }
        //执行任务回调(可选)
        Consumer<String> callback = task.getCallback();
        if (callback != null) {
            if (task.isSuccess()) {
                callback.accept(task.getPath());
            } else {
                callback.accept(null);
            }
        }
    }
}
