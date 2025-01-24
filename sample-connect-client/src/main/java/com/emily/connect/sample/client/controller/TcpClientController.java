package com.emily.connect.sample.client.controller;

import com.emily.connect.client.ClientManager;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.core.utils.UUIDUtils;
import com.emily.connect.sample.client.entity.User;
import com.emily.infrastructure.json.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author :  Emily
 * @since :  2025/1/17 上午9:45
 */
@RestController
public class TcpClientController {

    @GetMapping("api/tcp/client")
    public ResponseEntity getObj(@RequestBody User user, HttpServletRequest request) throws IOException {
        RequestHeader requestHeader = new RequestHeader()
                .traceId(UUIDUtils.randomSimpleUUID())
                .appType(request.getHeader("appType"))
                .appVersion(request.getHeader("appVersion"))
                .systemNumber("Emily-Sdk")
                .contentType((byte) 0)
                .url("/api/user/getUser")
                .method("POST");
        return ClientManager.getConnection().getForEntity("test", requestHeader, new RequestPayload(JsonUtils.toJSONString(user)));
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
                .url("/api/user/hello")
                .method("get");
        return ClientManager.getConnection().getForEntity("test", requestHeader,
                new RequestPayload("田晓霞"),
                new RequestPayload("18"),
                new RequestPayload("2.3sd"),
                new RequestPayload("2.3")
        );
    }
}
