package com.emily.connect.server;

import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;
import com.emily.connect.server.plugin.RequestJsonPlugin;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author :  Emily
 * @since :  2025/1/16 下午2:24
 */
@AutoConfiguration
public class NettyServerAutoConfiguration implements InitializingBean, DisposableBean {
    private static final Logger LOG = LoggerFactory.getLogger(NettyServerAutoConfiguration.class);

    @Bean
    public RequestJsonPlugin requestJsonPlugin(RequestMappingHandlerMapping handlerMapping, Validator validator) {
        RequestJsonPlugin requestJsonPlugin = new RequestJsonPlugin(handlerMapping, validator);
        PluginRegistry.registerPlugin(PluginType.JSON, requestJsonPlugin);
        return requestJsonPlugin;
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
