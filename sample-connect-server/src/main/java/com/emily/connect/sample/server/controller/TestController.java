package com.emily.connect.sample.server.controller;

import com.emily.connect.sample.server.entity.User;
import com.emily.connect.sample.server.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author :  Emily
 * @since :  2025/1/16 上午9:34
 */
@RestController
public class TestController {


    @PostMapping("api/user/getUser")
    public User getUser(@Validated @RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getHeader("traceId"));
        System.out.println(RequestUtils.getRequest().getHeader("traceId"));
        return user;
    }

    @GetMapping("api/user/hello")
    @ResponseBody
    public String hello(@RequestParam String name, Integer height, double line) {
        return "Hello, " + name;
    }

}
