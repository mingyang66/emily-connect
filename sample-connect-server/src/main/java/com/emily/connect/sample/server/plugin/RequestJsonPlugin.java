package com.emily.connect.sample.server.plugin;

import com.emily.connect.core.constant.MessageType;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.connect.server.plugin.Plugin;
import com.emily.connect.server.plugin.PluginType;
import com.emily.infrastructure.json.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author :  Emily
 * @since :  2025/1/15 下午4:24
 */
@Component
public class RequestJsonPlugin implements Plugin<String> {
    private final RequestMappingHandlerMapping handlerMapping;

    public RequestJsonPlugin(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    // 检查类型是否是原始类型或包装类型
    public static boolean isWrapper(Class<?> type) {
        if (Byte.class.isAssignableFrom(type)) {
            return true;
        } else if (Short.class.isAssignableFrom(type)) {
            return true;
        } else if (Integer.class.isAssignableFrom(type)) {
            return true;
        } else if (Long.class.isAssignableFrom(type)) {
            return true;
        } else if (Float.class.isAssignableFrom(type)) {
            return true;
        } else if (Double.class.isAssignableFrom(type)) {
            return true;
        } else if (Boolean.class.isAssignableFrom(type)) {
            return true;
        } else return Character.class.isAssignableFrom(type);
    }

    public static Object toPrimitive(String value, Class<?> type) {
        if (type == byte.class) {
            return StringUtils.isBlank(value) ? (byte) 0 : Byte.parseByte(value);
        } else if (type == short.class) {
            return StringUtils.isBlank(value) ? (short) 0 : Short.parseShort(value);
        } else if (type == int.class) {
            return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
        } else if (type == long.class) {
            return StringUtils.isBlank(value) ? 0L : Long.parseLong(value);
        } else if (type == float.class) {
            return StringUtils.isBlank(value) ? 0.0f : Float.parseFloat(value);
        } else if (type == double.class) {
            return StringUtils.isBlank(value) ? 0.0d : Double.parseDouble(value);
        } else if (type == char.class) {
            return StringUtils.isBlank(value) ? "\\u0000" : value.charAt(0);
        } else if (type == boolean.class) {
            return !StringUtils.isBlank(value) && Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("Unsupported Parameter Type：" + type.getName());
    }

    @Override
    public boolean supports(PluginType pluginType) {
        return pluginType == PluginType.JSON;
    }

    @Override
    public ResponseEntity invoke(RequestHeader header, RequestPayload... payload) throws Throwable {
        //创建模拟请求对象
        MockHttpServletRequest request = new MockHttpServletRequest(header.getMethod().toUpperCase(), header.getAction());
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        //将请求头设置到模拟请求对象
        Map<String, String> headers = BeanUtils.describe(header);
        headers.keySet().forEach(k -> request.addHeader(k, headers.get(k)));
        //创建模拟响应对象
        MockHttpServletResponse response = new MockHttpServletResponse();
        //请求上下文初始化
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        this.initContextHolders(request, attributes);
        try {
            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            ResponseEntity entity = new ResponseEntity().prefix(MessageType.REQUEST);
            if (Objects.isNull(chain)) {
                return entity.status(10000).message("请求接口不存在");
            } else if (chain.getHandler() instanceof HandlerMethod handlerMethod) {
                // 获取控制器对象 (bean)
                Object controller = handlerMethod.getBean();
                System.out.println("Controller bean: " + controller);
                // 获取方法对象
                Method method = handlerMethod.getMethod();
                System.out.println("Method: " + method.getName());
                // 获取方法参数类型
                Class<?>[] parameterTypes = method.getParameterTypes();
                int j = 0;
                Object[] args = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                        args[i] = request;
                    } else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                        args[i] = response;
                    } else if (String.class.isAssignableFrom(parameterType)) {
                        args[i] = payload[j++].getValue();
                    } else if (parameterType.isPrimitive()) {
                        String value = payload[j++].getValue();
                        args[i] = toPrimitive(value, parameterType);
                    } else if (isWrapper(parameterType)) {
                        String value = payload[j++].getValue();
                        args[i] = value == null ? null : JsonUtils.toJavaBean(value, parameterType);
                    } else {
                        String value = payload[j++].getValue();
                        args[i] = value == null ? null : JsonUtils.toJavaBean(value, parameterType);
                    }
                }
                // 调用控制器方法
                Object result = method.invoke(controller, args);
                System.out.println("Result: " + result);
                return entity.status(0).message("success").data(result);
            } else {
                return entity.status(10000).message("请求接口不存在");
            }
        } finally {
            //请求完成，销毁请求上下文对象
            this.resetContextHolders(attributes);
        }
    }

    private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
        LocaleContextHolder.setLocale(request.getLocale(), false);
        RequestContextHolder.setRequestAttributes(requestAttributes, false);

    }

    private void resetContextHolders(ServletRequestAttributes requestAttributes) {
        LocaleContextHolder.resetLocaleContext();
        RequestContextHolder.resetRequestAttributes();
        requestAttributes.requestCompleted();
    }
}
