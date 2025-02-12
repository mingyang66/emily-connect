package com.emily.connect.server.plugin;

import com.emily.connect.core.constant.PrefixType;
import com.emily.connect.core.entity.RequestHeader;
import com.emily.connect.core.entity.RequestPayload;
import com.emily.connect.core.entity.ResponseEntity;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.infrastructure.web.exception.entity.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author :  Emily
 * @since :  2025/1/15 下午4:24
 */
public class TcpRequestPlugin implements Plugin<String> {
    private final RequestMappingHandlerMapping handlerMapping;
    private final Validator validator;

    public TcpRequestPlugin(RequestMappingHandlerMapping handlerMapping, Validator validator) {
        this.handlerMapping = handlerMapping;
        this.validator = validator;
    }


    @Override
    public boolean supports(PluginType pluginType) {
        return pluginType == PluginType.TCP;
    }

    @Override
    public ResponseEntity invoke(RequestHeader header, RequestPayload... payload) throws Throwable {
        //创建模拟请求对象
        HttpServletRequest request = this.initHttpServletRequest(header);
        //创建模拟响应对象
        MockHttpServletResponse response = new MockHttpServletResponse();
        //请求上下文初始化
        ServletRequestAttributes attributes = this.initContextHolders(request, response);
        try {
            HandlerExecutionChain chain = handlerMapping.getHandler(request);
            if (Objects.nonNull(chain) && chain.getHandler() instanceof HandlerMethod handlerMethod) {
                // 获取控制器对象 (bean)
                Object controller = handlerMethod.getBean();
                // 获取方法对象
                Method method = handlerMethod.getMethod();
                // 获取参数对象
                Object[] args = getArgs(method, request, response, payload);
                // 参数校验
                validateParameter(args);
                // 调用接口
                return new ResponseEntity().prefix(PrefixType.TCP).status(0).message("success").data(method.invoke(controller, args));
            } else {
                throw new BusinessException(10000, "请求接口不存在");
            }
        } finally {
            // 请求完成，销毁请求上下文对象
            this.resetContextHolders(attributes);
        }
    }

    private void validateParameter(Object[] args) {
        if (args.length > 0) {
            Set<ConstraintViolation<Object>> validates = validator.validate(args[0]);
            if (!validates.isEmpty()) {
                // 处理验证错误
                StringBuilder errorMessage = new StringBuilder("Validation failed for Parameter: ");
                for (ConstraintViolation<Object> violation : validates) {
                    errorMessage.append(violation.getPropertyPath())
                            .append(" ")
                            .append(violation.getMessage())
                            .append("; ");
                }
                throw new BusinessException(10000, errorMessage.toString());
            }
        }
    }

    private Object[] getArgs(Method method, HttpServletRequest request, HttpServletResponse response, RequestPayload... payload) {
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
            } else {
                String value = payload[j++].getValue();
                args[i] = value == null ? null : JsonUtils.toJavaBean(value, parameterType);
            }
        }
        return args;
    }

    private HttpServletRequest initHttpServletRequest(RequestHeader header) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //创建模拟请求对象
        MockHttpServletRequest request = new MockHttpServletRequest(header.getMethod().toUpperCase(), header.getUrl());
        request.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        //将请求头设置到模拟请求对象
        Map<String, String> headers = BeanUtils.describe(header);
        headers.keySet().forEach(k -> request.addHeader(k, headers.get(k) == null ? "" : headers.get(k)));
        return request;
    }

    private ServletRequestAttributes initContextHolders(HttpServletRequest request, HttpServletResponse response) {
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        LocaleContextHolder.setLocale(request.getLocale(), false);
        RequestContextHolder.setRequestAttributes(attributes, false);
        return attributes;
    }

    private void resetContextHolders(ServletRequestAttributes requestAttributes) {
        LocaleContextHolder.resetLocaleContext();
        RequestContextHolder.resetRequestAttributes();
        requestAttributes.requestCompleted();
    }
}
