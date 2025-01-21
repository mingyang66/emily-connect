package com.emily.connect.client;


import java.time.Duration;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC客户端属性配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class ClientProperties {
    /**
     * 服务器地址，支持集群配置
     */
    private Map<String, String> config;
    /**
     * 请求超时时间，默认：60秒
     */
    private Duration readTimeOut = Duration.ofSeconds(60);
    /**
     * 连接超时时间，默认：5秒
     */
    private Duration connectTimeOut = Duration.ofSeconds(5);
    /**
     * 超过多长时间未发生读写就发送一次心跳包，默认：30秒
     */
    private Duration idleTimeOut = Duration.ofSeconds(30);
    /**
     * 连接池中的最大连接数，默认：5
     */
    private int maxConnections = 5;

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public Duration getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(Duration readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public Duration getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Duration connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Duration getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(Duration idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
