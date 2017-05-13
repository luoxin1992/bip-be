/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import cn.com.lx1992.lib.config.meta.IServletConfigItem;
import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * Servlet配置项
 *
 * @author luoxin
 * @version 2017-5-13
 */
@Component
public class ServletConfigItem implements IServletConfigItem {
    /**
     * 端口号
     */
    private int port;
    /**
     * 上下文路径
     */
    private String contextPath;
    /**
     * 静态文件路径
     */
    private String documentRoot;

    @Override
    @DisconfItem(key = "servlet.port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    @DisconfItem(key = "servlet.context.path")
    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    @DisconfItem(key = "servlet.document.root")
    public String getDocumentRoot() {
        return documentRoot;
    }

    public void setDocumentRoot(String documentRoot) {
        this.documentRoot = documentRoot;
    }
}
