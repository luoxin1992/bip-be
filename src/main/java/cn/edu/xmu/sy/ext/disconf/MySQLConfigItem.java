/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.disconf;

import cn.com.lx1992.lib.config.meta.IMySQLConfigItem;
import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * MySQL配置项
 * <p>
 * JDBC
 * 连接池
 * Druid
 *
 * @author luoxin
 * @version 2017-5-13
 */
@Component
public class MySQLConfigItem implements IMySQLConfigItem {
    /**
     * JDBC连接串
     */
    private String jdbcUrl;
    /**
     * JDBC用户名
     */
    private String jdbcUsername;
    /**
     * JDBC密码
     */
    private String jdbcPassword;

    /**
     * 连接池初始大小
     */
    private int poolInitialSize;
    /**
     * 连接池最小空闲数
     */
    private int poolMinIdle;
    /**
     * 连接池最大连接数
     */
    private int poolMaxActive;
    /**
     * 连接池获取连接最大等待时间
     */
    private int poolMaxWait;
    /**
     * Druid管理页用户名
     */
    private String druidUsername;
    /**
     * Druid管理页密码
     */
    private String druidPassword;

    @Override
    @DisconfItem(key = "mysql.jdbc.url")
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    @DisconfItem(key = "mysql.jdbc.username")
    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    @Override
    @DisconfItem(key = "mysql.jdbc.password")
    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    @Override
    @DisconfItem(key = "mysql.pool.initial.size")
    public int getPoolInitialSize() {
        return poolInitialSize;
    }

    public void setPoolInitialSize(int poolInitialSize) {
        this.poolInitialSize = poolInitialSize;
    }

    @Override
    @DisconfItem(key = "mysql.pool.min.idle")
    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    @Override
    @DisconfItem(key = "mysql.pool.max.active")
    public int getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(int poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    @Override
    @DisconfItem(key = "mysql.pool.max.wait")
    public int getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(int poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    @Override
    @DisconfItem(key = "mysql.druid.username")
    public String getDruidUsername() {
        return druidUsername;
    }

    public void setDruidUsername(String druidUsername) {
        this.druidUsername = druidUsername;
    }

    @Override
    @DisconfItem(key = "mysql.druid.password")
    public String getDruidPassword() {
        return druidPassword;
    }

    public void setDruidPassword(String druidPassword) {
        this.druidPassword = druidPassword;
    }
}
