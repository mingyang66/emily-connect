package com.emily.connect.server;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @program: emily-connect
 * @description: RPC服务端配置
 * @author: Emily
 * @create: 2021/09/22
 */
@ConfigurationProperties(prefix = TcpServerProperties.PREFIX)
public class TcpServerProperties {
    /**
     * 元数据前缀
     */
    public static final String PREFIX = "spring.emily.tcp";
    /**
     * 端口号,默认：9999
     */
    private int port = 9999;
    /**
     * 超过多长时间未发生读写就发送一次心跳包，默认：30秒
     */
    private Duration idleTimeOut = Duration.ofSeconds(30);
    /**
     * 允许客户端最大未响应心跳次数
     */
    private int maxUnResponseHeartbeats = 3;

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

    public int getMaxUnResponseHeartbeats() {
        return maxUnResponseHeartbeats;
    }

    public void setMaxUnResponseHeartbeats(int maxUnResponseHeartbeats) {
        this.maxUnResponseHeartbeats = maxUnResponseHeartbeats;
    }
}
