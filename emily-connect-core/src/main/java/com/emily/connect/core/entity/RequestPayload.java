package com.emily.connect.core.entity;

/**
 * @author :  Emily
 * @since :  2025/1/20 下午3:01
 */
public class RequestPayload {

    private String value;

    public RequestPayload(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
