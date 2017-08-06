/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.disconf;

import cn.com.lx1992.lib.config.meta.IRedisConfigItem;
import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * Redis配置项
 *
 * @author luoxin
 * @version 2017-5-22
 */
@Component
public class RedisConfigItem implements IRedisConfigItem {
    /**
     * 主机地址
     */
    private String host;
    /**
     * 端口号
     */
    private int port;
    /**
     * 连接池最大连接数
     */
    private int poolMax;
    /**
     * 连接池最大等待时间
     */
    private int poolMaxWait;
    /**
     * 连接池最大空闲连接数
     */
    private int poolMaxIdle;
    /**
     * 连接池最小空闲连接数
     */
    private int poolMinIdle;

    @Override
    @DisconfItem(key = "redis.host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    @DisconfItem(key = "redis.port")
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    @DisconfItem(key = "redis.pool.max")
    public int getPoolMax() {
        return poolMax;
    }

    public void setPoolMax(int poolMax) {
        this.poolMax = poolMax;
    }

    @Override
    @DisconfItem(key = "redis.pool.max.wait")
    public int getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(int poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    @Override
    @DisconfItem(key = "redis.pool.max.idle")
    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    @Override
    @DisconfItem(key = "redis.pool.min.idle")
    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }
}
