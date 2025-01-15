package com.emily.connect.sample.server;


import com.emily.connect.server.ServerConnection;
import com.emily.connect.server.ServerProperties;
import com.emily.connect.server.plugin.ApplicationJsonPlugin;
import com.emily.connect.server.plugin.PluginRegistry;
import com.emily.connect.server.plugin.PluginType;

/**
 * @author Emily
 */
public class ConnectServerBootStrap {
    public static void main(String[] args) {
        ServerProperties properties = new ServerProperties();
        PluginRegistry.registerPlugin(PluginType.BEAN, new ApplicationJsonPlugin());
        new ServerConnection(properties).start();
    }

}
