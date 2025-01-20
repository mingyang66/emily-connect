package com.emily.connect.core.entity;

/**
 * @author :  Emily
 * @since :  2025/1/17 下午1:31
 */
public class RequestEntity {
    private byte prefix;
    private RequestHeader headers;
    private RequestPayload[] payload;

    public byte getPrefix() {
        return prefix;
    }

    public void setPrefix(byte prefix) {
        this.prefix = prefix;
    }

    public RequestHeader getHeaders() {
        return headers;
    }

    public void setHeaders(RequestHeader headers) {
        this.headers = headers;
    }

    public RequestPayload[] getPayload() {
        return payload;
    }

    public void setPayload(RequestPayload[] payload) {
        this.payload = payload;
    }

    public RequestEntity prefix(byte prefix) {
        this.prefix = prefix;
        return this;
    }

    public RequestEntity headers(RequestHeader headers) {
        this.headers = headers;
        return this;
    }

    public RequestEntity payload(RequestPayload... payload) {
        this.payload = payload;
        return this;
    }
}
