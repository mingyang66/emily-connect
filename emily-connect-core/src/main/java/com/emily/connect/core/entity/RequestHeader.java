package com.emily.connect.core.entity;

import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author :  Emily
 * @since :  2025/1/10 下午3:03
 */
public class RequestHeader {
    private String systemNumber;
    private String traceId;
    private String appType;
    private String appVersion;
    /**
     * 请求体类型，0-json
     */
    private byte contentType;
    private String url;
    private String method;
    /**
     * 请求超时时间，单位：秒
     */
    private int timeout;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSystemNumber() {
        return systemNumber;
    }

    public void setSystemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte getContentType() {
        return contentType;
    }

    public void setContentType(byte contentType) {
        this.contentType = contentType;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public RequestHeader traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public RequestHeader appType(String appType) {
        this.appType = appType;
        return this;
    }

    public RequestHeader appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public RequestHeader systemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
        return this;
    }

    public RequestHeader contentType(byte contentType) {
        this.contentType = contentType;
        return this;
    }

    public RequestHeader url(String url) {
        this.url = url;
        return this;
    }

    public RequestHeader method(String method) {
        this.method = method;
        return this;
    }

    public RequestHeader timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public byte[] toByteArray() {
        // 创建一个ByteBuf实例
        ByteBuf byteBuf = Unpooled.buffer();
        // 将每个字符串写入ByteBuf
        ByteBufUtils.writeString(byteBuf, this.systemNumber);
        ByteBufUtils.writeString(byteBuf, this.traceId);
        ByteBufUtils.writeString(byteBuf, this.appType);
        ByteBufUtils.writeString(byteBuf, this.appVersion);
        byteBuf.writeByte(this.contentType);
        ByteBufUtils.writeString(byteBuf, this.url);
        ByteBufUtils.writeString(byteBuf, this.method);
        byteBuf.writeInt(this.timeout);
        // 创建一个字节数组来存储ByteBuf中的数据
        byte[] bytes = ByteBufUtils.readBytes(byteBuf);
        // 释放ByteBuf的资源
        byteBuf.release();
        return bytes;
    }
}
