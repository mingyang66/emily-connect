package com.emily.connect.server.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author :  姚明洋
 * @since :  2025/1/15 下午4:54
 */
public class PluginRegistry {
    public static final Map<PluginType, Plugin<?>> plugins = new ConcurrentHashMap<>();

    public static void registerPlugin(PluginType pluginType, Plugin<?> plugin) {
        plugins.putIfAbsent(pluginType, plugin);
    }

    public static Plugin<?> getPlugin(PluginType pluginType) {
        return plugins.get(pluginType);
    }
}
