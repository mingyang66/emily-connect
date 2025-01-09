package com.emily.connect.server.manager;

import com.emily.connect.datasource.pool.DataSourceProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC服务端配置
 * @author: Emily
 * @create: 2021/09/22
 */
public class DbServerProperties {
    /**
     * 端口号,默认：9999
     */
    private int port = 9999;
    /**
     * 超过多长时间未发生读写就发送一次心跳包，默认：30秒
     */
    private Duration idleTimeOut = Duration.ofSeconds(30);
    /**
     * 数据库属性配置
     */
    private Map<String, DataSourceProperties> config = new HashMap<>();

    public Map<String, DataSourceProperties> getConfig() {
        return config;
    }

    public void setConfig(Map<String, DataSourceProperties> config) {
        this.config = config;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Duration getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(Duration idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }
}
