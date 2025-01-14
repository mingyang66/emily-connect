package com.emily.connect.core.protocol;

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
}
