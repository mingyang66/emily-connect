package com.emily.connect.server.handler;

import com.emily.connect.core.protocol.TransContent;

import java.io.IOException;

/**
 * @Description :  后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/24 1:34 PM
 */
public interface DbBusinessHandler {
    /**
     * 自定义处理后置业务
     *
     * @param content
     * @return
     * @throws IOException
     */
    Object handler(TransContent content);
}
