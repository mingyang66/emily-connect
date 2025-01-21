package com.emily.connect.sample.client.controller;

import com.emily.connect.client.ClientConnection;
import com.emily.connect.client.ClientManager;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.UUIDUtils;
import com.emily.connect.sample.client.entity.User;
import com.emily.infrastructure.json.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author :  Emily
 * @since :  2025/1/17 上午9:45
 */
@RestController
public class TcpClientController {
    private static final ClientConnection connection = ClientManager.getConnection();

    @GetMapping("api/tcp/client")
    public Object getObj() throws IOException {
        User user = new User();
        user.setUsername("account");
        user.setPassword("select_test_dual");
        user.setAge(10);

        RequestHeader requestHeader = new RequestHeader()
                .traceId(UUIDUtils.randomSimpleUUID())
                .appType("com.android")
                .appVersion("6.8")
                .systemNumber("Emily-Sdk")
                .contentType((byte) 0)
                .action("/api/user/getUser")
                .method("POST");
        ResponseEntity entity = connection.getForEntity("test", requestHeader, new RequestPayload(JsonUtils.toJSONString(user)));
        return entity.getData();
    }

    @GetMapping("api/tcp/hello")
    public Object getHello() throws IOException {
        User user = new User();
        user.setUsername("account");
        user.setPassword("select_test_dual");
        user.setAge(10);

        RequestHeader requestHeader = new RequestHeader()
                .traceId(UUIDUtils.randomSimpleUUID())
                .appType("com.android")
                .appVersion("6.8")
                .systemNumber("Emily-Sdk")
                .contentType((byte) 0)
                .action("/api/user/hello")
                .method("get");
        ResponseEntity entity = connection.getForEntity("test", requestHeader, new RequestPayload("田晓霞"), new RequestPayload("18"), new RequestPayload("2.3"));
        return entity.getData();
    }
}
