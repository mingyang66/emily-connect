package com.emily.connect.server;

import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import com.emily.connect.server.plugin.ServletRequestPlugin;
import com.emily.connect.server.plugin.TcpRequestPlugin;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author :  Emily
 * @since :  2025/1/16 下午2:24
 */
@AutoConfiguration
public class NettyServerAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServerAutoConfiguration.class);

    @Bean
    public ServletRequestPlugin servletRequestPlugin(RequestMappingHandlerMapping handlerMapping, Validator validator) {
        ServletRequestPlugin servletRequestPlugin = new ServletRequestPlugin(handlerMapping, validator);
        PluginRegistry.registerPlugin(PluginType.SERVLET, servletRequestPlugin);
        return servletRequestPlugin;
    }

    @Bean
    public TcpRequestPlugin tcpRequestPlugin(RequestMappingHandlerMapping handlerMapping, Validator validator) {
        TcpRequestPlugin tcpRequestPlugin = new TcpRequestPlugin(handlerMapping, validator);
        PluginRegistry.registerPlugin(PluginType.TCP, tcpRequestPlugin);
        return tcpRequestPlugin;
    }

    @Override
    public void destroy() {
        LOG.info("<== 【销毁--自动化配置】----数据脱敏组件【NettyServerAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        LOG.info("==> 【初始化--自动化配置】----数据脱敏组件【NettyServerAutoConfiguration】");
    }

}
