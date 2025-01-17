package com.emily.connect.core.entity;

/**
 * @author :  Emily
 * @since :  2025/1/10 下午3:03
 */
public class ResponseHeader {
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

    public ResponseHeader traceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public ResponseHeader appType(String appType) {
        this.appType = appType;
        return this;
    }

    public ResponseHeader appVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public ResponseHeader systemNumber(String systemNumber) {
        this.systemNumber = systemNumber;
        return this;
    }
}
