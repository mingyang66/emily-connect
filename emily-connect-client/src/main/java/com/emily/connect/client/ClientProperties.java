package com.emily.connect.client;


import java.time.Duration;
import java.util.Arrays;
import java.util.List;

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
    private List<Address> address = Arrays.asList(new Address(), new Address("172.30.71.95", 9999));
    /**
     * 请求超时时间，默认：5秒
     */
    private Duration readTimeOut = Duration.ofSeconds(5);
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

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
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

    public static class Address {
        /**
         * ip地址，默认：127.0.0.1
         */
        private String ip = "127.0.0.1";
        /**
         * 端口号 默认：9999
         */
        private int port = 9999;

        public Address() {
        }

        public Address(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
