package com.emily.connect.sample.client.entity;


/**
 * @program: SkyDb
 * @description: 自定义RPC传输协议
 * @author: Emily
 * @create: 2021/09/17
 */
public class RequestBody {

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
