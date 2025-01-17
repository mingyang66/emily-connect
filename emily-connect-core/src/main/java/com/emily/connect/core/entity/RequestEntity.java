package com.emily.connect.core.entity;

/**
 * @author :  姚明洋
 * @since :  2025/1/17 下午1:31
 */
public class RequestEntity {
    private byte prefix;
    private RequestHeader headers;
    private byte[] body;

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

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public RequestEntity prefix(byte prefix) {
        this.prefix = prefix;
        return this;
    }

    public RequestEntity headers(RequestHeader headers) {
        this.headers = headers;
        return this;
    }

    public RequestEntity body(byte[] body) {
        this.body = body;
        return this;
    }
}
