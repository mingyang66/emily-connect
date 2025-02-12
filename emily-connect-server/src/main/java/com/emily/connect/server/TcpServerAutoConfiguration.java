package com.emily.connect.server;

import com.emily.connect.server.connection.ServerConnection;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import com.emily.connect.server.plugin.ServletRequestPlugin;
import com.emily.connect.server.plugin.TcpRequestPlugin;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author :  Emily
 * @since :  2025/1/16 下午2:24
 */
@AutoConfiguration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableConfigurationProperties(TcpServerProperties.class)
@ConditionalOnProperty(prefix = TcpServerProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class TcpServerAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(TcpServerAutoConfiguration.class);
    private final TcpServerProperties properties;
    private final RequestMappingHandlerMapping handlerMapping;
    private final Validator validator;

    public TcpServerAutoConfiguration(TcpServerProperties properties, RequestMappingHandlerMapping handlerMapping, Validator validator) {
        this.properties = properties;
        this.handlerMapping = handlerMapping;
        this.validator = validator;
    }

    @Bean(initMethod = "start")
    public ServerConnection serverConnection() {
        PluginRegistry.registerPlugin(PluginType.SERVLET, new ServletRequestPlugin(handlerMapping, validator));
        PluginRegistry.registerPlugin(PluginType.TCP, new TcpRequestPlugin(handlerMapping, validator));
        return new ServerConnection(properties);
    }

    @Override
    public void destroy() {
        LOG.info("<== 【销毁--自动化配置】----TCP服务端组件【TcpServerAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        LOG.info("==> 【初始化--自动化配置】----TCP服务端组件【TcpServerAutoConfiguration】");
    }

}
