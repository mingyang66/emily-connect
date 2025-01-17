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
    private byte contentType;
    private String action;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public byte getContentType() {
        return contentType;
    }

    public void setContentType(byte contentType) {
        this.contentType = contentType;
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

    public RequestHeader action(String action) {
        this.action = action;
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
        ByteBufUtils.writeString(byteBuf, this.action);
        // 创建一个字节数组来存储ByteBuf中的数据
        byte[] byteArray = ByteBufUtils.readBytes(byteBuf);
        // 释放ByteBuf的资源
        byteBuf.release();
        return byteArray;
    }
}
