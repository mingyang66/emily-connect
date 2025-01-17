package com.emily.connect.sample.client.controller;

import com.emily.connect.client.ClientConnection;
import com.emily.connect.client.ClientProperties;
import com.emily.connect.core.protocol.RequestBody;
import com.emily.connect.core.protocol.RequestHeader;
import com.emily.connect.core.utils.UUIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author :  姚明洋
 * @since :  2025/1/17 上午9:45
 */
@RestController
public class TcpClientController {
    private static final ClientConnection connection = new ClientConnection(new ClientProperties());

    @GetMapping("api/tcp/client")
    public Object getObj() throws IOException {
        RequestBody requestBody = new RequestBody();
        requestBody.setUsername("account");
        requestBody.setPassword("select_test_dual");
        requestBody.setAge(10);

        RequestHeader requestHeader = new RequestHeader()
                .traceId(UUIDUtils.randomSimpleUUID())
                .appType("com.android")
                .appVersion("6.8")
                .systemNumber("Emily-Sdk")
                .contentType((byte) 0)
                .action("/api/user/getUser");
        String str = connection.getForEntity(requestHeader, requestBody, new TypeReference<>() {
        });
        return str;
    }
}
