package com.emily.connect.core.entity;

/**
 * @author :  姚明洋
 * @since :  2025/1/20 下午3:01
 */
public class RequestPayload {
    private String name;
    private String value;

    public RequestPayload(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
