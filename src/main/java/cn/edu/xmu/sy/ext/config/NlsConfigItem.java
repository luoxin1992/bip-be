/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * 阿里云数加NLS配置项
 *
 * @author luoxin
 * @version 2017-5-15
 */
@Component
public class NlsConfigItem {
    /**
     * APP KEY
     */
    private String appKey;
    /**
     * Access Key ID
     */
    private String accessKeyId;
    /**
     * Access Key Secret
     */
    private String accessKeySecret;
    /**
     * 线程池关闭超时
     */
    private int executorShutdownTimeout;

    @DisconfItem(key = "nls.app.key")
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @DisconfItem(key = "nls.access.key.id")
    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    @DisconfItem(key = "nls.access.key.secret")
    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    @DisconfItem(key = "nls.executor.shutdown.timeout")
    public int getExecutorShutdownTimeout() {
        return executorShutdownTimeout;
    }

    public void setExecutorShutdownTimeout(int executorShutdownTimeout) {
        this.executorShutdownTimeout = executorShutdownTimeout;
    }
}
