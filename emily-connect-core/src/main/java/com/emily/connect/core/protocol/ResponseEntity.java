package com.emily.connect.core.protocol;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class ResponseEntity {
    /**
     * 响应包类型，0-正常响应，1-心跳包
     */
    private byte packageType = 0;
    /**
     * 请求唯一标识，34个字节长度
     */
    private byte[] headers;
    /**
     * 消息
     */
    private byte[] body;

    public byte getPackageType() {
        return packageType;
    }

    public void setPackageType(byte packageType) {
        this.packageType = packageType;
    }

    public byte[] getHeaders() {
        return headers;
    }

    public void setHeaders(byte[] headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public ResponseEntity packageType(byte packageType) {
        this.packageType = packageType;
        return this;
    }

    public ResponseEntity headers(byte[] headers) {
        this.headers = headers;
        return this;
    }

    public ResponseEntity body(byte[] body) {
        this.body = body;
        return this;
    }
}
