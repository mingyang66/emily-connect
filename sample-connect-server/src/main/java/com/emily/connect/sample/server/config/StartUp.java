package com.emily.connect.sample.server.config;

import com.emily.connect.sample.server.plugin.ApplicationJsonPlugin;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  姚明洋
 * @since :  2025/1/16 下午2:24
 */
@Configuration
public class StartUp {
    private final ApplicationContext context;

    public StartUp(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    public void init() {
        PluginRegistry.registerPlugin(PluginType.BEAN, context.getBean(ApplicationJsonPlugin.class));
    }
}
