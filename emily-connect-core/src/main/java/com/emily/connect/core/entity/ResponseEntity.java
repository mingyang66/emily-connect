package com.emily.connect.core.entity;

/**
 * @author :  Emily
 * @since :  2025/1/17 下午7:06
 */
public class ResponseEntity {
    private byte prefix;
    private int status;
    private String message;
    private Object data;

    public byte getPrefix() {
        return prefix;
    }

    public void setPrefix(byte prefix) {
        this.prefix = prefix;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseEntity prefix(byte prefix) {
        this.prefix = prefix;
        return this;
    }

    public ResponseEntity status(int status) {
        this.status = status;
        return this;
    }

    public ResponseEntity message(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntity data(Object data) {
        this.data = data;
        return this;
    }

}
