package com.emily.connect.sample.server.plugin;

import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginType;
import com.emily.infrastructure.json.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author :  Emily
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
    public ResponseEntity invoke(RequestHeader header, RequestPayload... payload) throws Throwable {
        // 创建模拟请求
        MockHttpServletRequest request = new MockHttpServletRequest("POST", header.getAction());
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //request.setContent(payload);
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        MockHttpServletResponse response = new MockHttpServletResponse();
        //设置请求头
        Map<String, String> headers = BeanUtils.describe(header);
        headers.keySet().forEach(k -> request.addHeader(k, headers.get(k)));

        HandlerExecutionChain chain = handlerMapping.getHandler(request);

        ResponseEntity entity = new ResponseEntity().prefix((byte) 0);
        if (Objects.isNull(chain)) {
            return entity.status(10000).message("请求接口不存在");
        }
        if (chain.getHandler() instanceof HandlerMethod handlerMethod) {
            // 获取控制器对象 (bean)
            Object controller = handlerMethod.getBean();
            System.out.println("Controller bean: " + controller);
            // 获取方法对象
            Method method = handlerMethod.getMethod();
            System.out.println("Method: " + method.getName());
            List<Object> list = new ArrayList<>();
            // 获取方法参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            Parameter[] parameters = method.getParameters();
            int j = 0;
            Object[] args = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                    args[i] = request;
                } else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                    args[i] = response;
                } else if (isEntityClass(parameterType)) {
                    args[i] = JsonUtils.toJavaBean(payload[j++].getValue(), parameterTypes[0]);
                }

            }

            // 调用控制器方法
            Object result = method.invoke(controller, args);
            System.out.println("Result: " + result);
            return entity.status(0).message("success").data(result);
        }
        return entity.status(10000).message("请求接口不存在");
    }

    public static boolean isEntityClass(Class<?> clazz) {
        // 检查类是否包含至少一个字段
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return false;
        }

        // 检查类是否包含至少一个方法
        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length == 0) {
            return false;
        }

        // 进一步检查方法是否包含 getter 和 setter
        boolean hasGetter = false;
        boolean hasSetter = false;
        for (Method method : methods) {
            if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                hasGetter = true;
            }
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                hasSetter = true;
            }
        }

        return hasGetter && hasSetter;
    }
}
