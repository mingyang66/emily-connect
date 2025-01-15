package com.emily.connect.server.handler;

import com.emily.connect.core.protocol.RequestBody;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class ServerBusinessHandler {
    public Object invoke(RequestBody content) {
        System.out.println("server received:" + content.getUsername() + "-" + content.getPassword() + "-" + content.getAge());
        return "处理成功...";
    }
}
