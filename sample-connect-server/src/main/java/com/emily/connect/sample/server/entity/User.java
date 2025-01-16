package com.emily.connect.sample.server.entity;

/**
 * @author :  Emily
 * @since :  2025/1/16 上午9:36
 */
public class User {
    /**
     * sql语句唯一标识
     */
    private String username;
    /**
     * 数据库标识
     */
    private String password;
    private int age;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
