package com.emily.connect.sample.server.plugin;

import com.emily.connect.core.protocol.RequestHeader;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginType;
import com.emily.infrastructure.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * @author :  姚明洋
 * @since :  2025/1/15 下午4:24
 */
@Component
public class ApplicationJsonPlugin implements Plugin<String> {
    private final RequestMappingHandlerMapping handlerMapping;

    public ApplicationJsonPlugin(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public boolean supports(PluginType pluginType) {
        return pluginType == PluginType.BEAN;
    }

    @Override
    public Object invoke(RequestHeader header, byte[] payload) {
        System.out.println("请求头：" + JsonUtils.toJSONString(header));
        System.out.println("server received:" + new String(payload, StandardCharsets.UTF_8));
        // 创建模拟请求
        MockHttpServletRequest request = new MockHttpServletRequest("POST", header.getAction());
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setContent(payload);

        try {
            // 获取处理请求的 HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
            // 获取控制器对象 (bean)
            Object controller = handlerMethod.getBean();
            System.out.println("Controller bean: " + controller);
            // 获取方法对象
            Method method = handlerMethod.getMethod();
            System.out.println("Method: " + method.getName());
            // 获取方法参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object user = JsonUtils.toJavaBean(new String(payload, StandardCharsets.UTF_8), parameterTypes[0]);
            // 调用控制器方法
            Object result = method.invoke(controller, user);
            System.out.println("Result: " + result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
