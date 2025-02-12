package com.emily.connect.server.plugin;

import com.emily.connect.core.constant.PrefixType;

/**
 * @author :  Emily
 * @since :  2025/1/15 下午4:56
 */
public enum PluginType {
    SERVLET(PrefixType.SERVLET), TCP(PrefixType.TCP);
    private final byte code;

    PluginType(byte code) {
        this.code = code;
    }

    public static PluginType getPluginTypeByCode(byte code) {
        for (PluginType pluginType : PluginType.values()) {
            if (pluginType.getCode() == code) {
                return pluginType;
            }
        }
        throw new IllegalArgumentException("No plugin type found for code " + code);
    }

    public byte getCode() {
        return code;
    }
}
