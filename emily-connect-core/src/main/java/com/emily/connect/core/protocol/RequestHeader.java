package com.emily.connect.core.protocol;

import com.emily.connect.core.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author :  姚明洋
 * @since :  2025/1/10 下午3:03
 */
public class RequestHeader {
    private String traceId;
    private String appType;
    private String appVersion;
    private String systemNumber;

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

    public byte[] toByteArray() {
        // 创建一个ByteBuf实例
        ByteBuf byteBuf = Unpooled.buffer();
        // 将每个字符串写入ByteBuf
        ByteBufUtils.writeString(byteBuf, this.traceId);
        ByteBufUtils.writeString(byteBuf, this.appType);
        ByteBufUtils.writeString(byteBuf, this.appVersion);
        ByteBufUtils.writeString(byteBuf, this.systemNumber);
        // 创建一个字节数组来存储ByteBuf中的数据
        byte[] byteArray = new byte[byteBuf.readableBytes()];
        // 将ByteBuf中的数据读到字节数组中
        byteBuf.readBytes(byteArray);
        // 释放ByteBuf的资源
        byteBuf.release();
        return byteArray;
    }
}
