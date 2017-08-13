/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.disconf;

import cn.com.lx1992.lib.config.meta.IDebugConfigItem;
import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * 调试配置项
 * <p>
 * 打印API日志(参数/结果)
 * 允许跨域访问
 *
 * @author luoxin
 * @version 2017-7-13
 */
@Component
public class DebugConfigItem implements IDebugConfigItem {
    /**
     * 允许打印API日志
     */
    private String apiLogEnable;
    /**
     * 允许跨域访问
     */
    private String crossDomainEnable;

    @Override
    @DisconfItem(key = "debug.api.log.enable")
    public String getApiLogEnable() {
        return apiLogEnable;
    }

    public void setApiLogEnable(String apiLogEnable) {
        this.apiLogEnable = apiLogEnable;
    }

    @Override
    @DisconfItem(key = "debug.cross.domain.enable")
    public String getCrossDomainEnable() {
        return crossDomainEnable;
    }

    public void setCrossDomainEnable(String crossDomainEnable) {
        this.crossDomainEnable = crossDomainEnable;
    }
}
