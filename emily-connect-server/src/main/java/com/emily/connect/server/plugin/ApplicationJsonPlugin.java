package com.emily.connect.server.plugin;

import com.emily.connect.core.protocol.RequestHeader;
import com.emily.infrastructure.json.JsonUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author :  姚明洋
 * @since :  2025/1/15 下午4:24
 */
public class ApplicationJsonPlugin implements Plugin<String> {
    @Override
    public boolean supports(PluginType pluginType) {
        return pluginType == PluginType.BEAN;
    }

    @Override
    public Object invoke(RequestHeader header, byte[] payload) {
        System.out.println("请求头：" + JsonUtils.toJSONString(header));
        System.out.println("server received:" + new String(payload, StandardCharsets.UTF_8));
        return "处理成功...";
    }
}
