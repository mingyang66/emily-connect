package com.emily.connect.server.plugin;

import com.emily.connect.core.entity.RequestHeader;

/**
 * @author :  Emily
 * @since :  2025/1/15 下午4:20
 */
public interface Plugin<T> {
    boolean supports(PluginType pluginType);

    Object invoke(RequestHeader header, byte[] payload) throws Throwable;
}
