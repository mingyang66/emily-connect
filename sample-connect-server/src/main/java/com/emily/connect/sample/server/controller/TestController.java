package com.emily.connect.sample.server.controller;

import com.emily.connect.sample.server.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author :  Emily
 * @since :  2025/1/16 上午9:34
 */
@RestController
public class TestController {


    @PostMapping("api/user/getUser")
    public User getUser(@Validated @RequestBody User user) {
        return user;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam String name) {
        return "Hello, " + name;
    }

}
