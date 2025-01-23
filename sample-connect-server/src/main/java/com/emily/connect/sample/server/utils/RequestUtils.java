package com.emily.connect.sample.server.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: HttpServlet
 * @Author: 姚明洋
 * @Date: 2019/5/21 13:16
 * @Version: 1.0
 */
public class RequestUtils {
    /**
     * unknown
     */
    private static final String UNKNOWN = "unknown";
    /**
     * 本机IP
     */
    private static final String LOCAL_IP = "127.0.0.1";
    /**
     * 服务器端IP
     */
    private static String SERVER_IP = null;

    /**
     * 获取请求真实IP,支持代理，如：179.156.81.168, 10.171.10.12，则取第一个IP 179.156.81.168
     *
     * @return 真实IP
     */
    public static String getRealClientIp() {
        String ip = getClientIp();
        return StringUtils.contains(ip, ",") ? StringUtils.split(ip, ",")[0] : ip;
    }

    /**
     * 获取客户端IP
     */
    public static String getClientIp() {
        if (isServletContext()) {
            return getClientIp(getRequest());
        }
        return LOCAL_IP;
    }

    /**
     * 获取客户单IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (StringUtils.equalsIgnoreCase("0:0:0:0:0:0:0:1", ip)) {
                ip = LOCAL_IP;
            }
            return ip;
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * 判断请求IP是否是内网IP
     */
    @Deprecated(since = "3.3.2", forRemoval = true)
    public static boolean isInnerIp(String ip) {
        String reg = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
                + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
                + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    /**
     *
     */
    public static boolean noInternet(String ip) {
        return !isInternet(ip);
    }

    /**
     * 判定是否是内网地址
     */
    public static boolean isInternet(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        if (StringUtils.equals("0:0:0:0:0:0:0:1", ip)) {
            return true;
        }
        Pattern reg = Pattern.compile("^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$");
        Matcher match = reg.matcher(ip);
        return match.find();
    }

    /**
     * 获取服务器端的IP
     */
    public static String getServerIp() {
        if (StringUtils.isNotEmpty(SERVER_IP)) {
            return SERVER_IP;
        }
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                String name = netInterface.getName();
                if (!StringUtils.contains(name, "docker") && !StringUtils.contains(name, "lo")) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        if (ip != null
                                && ip instanceof Inet4Address
                                && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(":") == -1) {
                            SERVER_IP = ip.getHostAddress();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            SERVER_IP = LOCAL_IP;
        }
        return SERVER_IP;
    }

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }

    /**
     * 是否存在servlet上下文
     */
    public static boolean isServletContext() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 获取用户当前请求的HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            assert attributes != null;
            return attributes.getRequest();
        } catch (Exception ex) {
            throw new IllegalArgumentException("非法访问");
        }
    }

    /**
     * 获取当前请求的HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            assert attributes != null;
            return attributes.getResponse();
        } catch (Exception ex) {
            throw new IllegalArgumentException("非法访问");
        }
    }

    /**
     * 开启请求时间记录
     */
    public static void startRequest() {
        if (!isServletContext()) {
            return;
        }
        //设置业务请求开始时间
        getRequest().setAttribute("startTime", System.currentTimeMillis());
    }

    /**
     * 获取请求开始到当前耗时
     */
    public static long getSpentTime() {
        if (!isServletContext()) {
            return 0;
        }
        if (getRequest().getAttribute("startTime") == null) {
            return 0;
        }
        return System.currentTimeMillis() - Long.parseLong(getRequest().getAttribute("startTime").toString());
    }

    /**
     * 获取请求头，请求头必须传
     *
     * @param header 请求头
     * @return 请求头结果
     */
    public static String getHeader(String header) {
        return getHeader(header, false);
    }

    /**
     * 获取请求头
     *
     * @param header   请求头
     * @param required 是否允许请求头为空或者不传递，true-是；false-否
     * @return 请求头结果
     */
    public static String getHeader(String header, boolean required) {
        if (header == null || header.isEmpty()) {
            return header;
        }
        String value = getRequest().getHeader(header);
        if (!required) {
            return value;
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("非法参数");
        }
        return value;
    }

    /**
     * 获取请求头，请求头不存在则返回默认值
     *
     * @param header       请求头名
     * @param defaultValue 默认值
     * @return 请求头
     */
    public static String getHeaderOrDefault(String header, String defaultValue) {
        String value = getHeader(header, false);
        if (value == null || value.isEmpty() || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    /**
     * 获取请求头
     *
     * @param request 请求servlet对象
     * @return 请求头集合对象
     */
    public static Map<String, Object> getHeaders(HttpServletRequest request) {
        Assert.notNull(request, "Illegal Parameter: request must not be null");
        Map<String, Object> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        Optional.ofNullable(headerNames).ifPresent(headerName -> {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headers.put(name, value);
            }
        });
        return headers;
    }

    /**
     * 获取Get、POST等URL后缀请求参数
     *
     * @param request 请求上下文
     * @return 参数集合
     */
    public static Map<String, Object> getParameters(HttpServletRequest request) {
        Assert.notNull(request, "Illegal Parameter: request must not be null");
        Enumeration<String> names = request.getParameterNames();
        if (names == null) {
            return Collections.emptyMap();
        }
        Map<String, Object> paramMap = new LinkedHashMap<>();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            if (!paramMap.containsKey(key)) {
                paramMap.put(key, request.getParameter(key));
            }
        }
        return paramMap;
    }

}
