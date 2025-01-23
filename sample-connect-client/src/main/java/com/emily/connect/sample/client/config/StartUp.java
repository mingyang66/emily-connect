package com.emily.connect.sample.client.config;

import com.emily.connect.client.ClientManager;
import com.emily.connect.client.ClientProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author :  Emily
 * @since :  2025/1/21 下午1:47
 */
@Configuration
public class StartUp {
    public StartUp() {
        ClientProperties properties = new ClientProperties();
        properties.setConfig(Map.of("test", "127.0.0.1:9999"));
        ClientManager.initConnection(properties);
    }
}
