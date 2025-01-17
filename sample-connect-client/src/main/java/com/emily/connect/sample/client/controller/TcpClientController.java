package com.emily.connect.sample.client.controller;

import com.emily.connect.client.ClientConnection;
import com.emily.connect.client.ClientProperties;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.UUIDUtils;
import com.emily.connect.sample.client.entity.RequestBody;
import com.emily.infrastructure.json.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author :  Emily
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
        byte[] payload = JsonUtils.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8);
        ResponseEntity entity = connection.getForEntity(requestHeader, payload);
        return entity.getData();
    }
}
