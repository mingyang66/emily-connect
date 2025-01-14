package com.emily.connect.core.protocol;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class RequestEntity {
    /**
     * 包类型，0-正常请求，1-心跳包
     */
    private byte packageType = 0;
    /**
     * 请求唯一标识，34个字节长度
     */
    private byte[] header;
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

    public byte[] getHeader() {
        return header;
    }

    public void setHeader(byte[] header) {
        this.header = header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public RequestEntity packageType(byte packageType) {
        this.packageType = packageType;
        return this;
    }

    public RequestEntity header(byte[] header) {
        this.header = header;
        return this;
    }

    public RequestEntity body(byte[] body) {
        this.body = body;
        return this;
    }
}
