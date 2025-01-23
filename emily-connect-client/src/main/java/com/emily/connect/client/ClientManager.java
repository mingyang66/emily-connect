package com.emily.connect.client;

import java.net.InetSocketAddress;

/**
 * @author :  Emily
 * @since :  2025/1/21 下午1:41
 */
public class ClientManager {
    private static ClientConnection connection;

    public static void initConnection(ClientProperties properties) {
        if (connection == null) {
            connection = new ClientConnection(properties);
        }
    }

    public static ClientConnection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Client not initialized");
        }
        return connection;
    }

    public static InetSocketAddress getSocketAddress(String tag) {
        if (connection == null) {
            throw new IllegalStateException("Client not initialized");
        }
        String address = connection.getProperties().getConfig().get(tag);
        if (address == null) {
            throw new IllegalStateException("Server address can not be config");
        }
        String host = address.split(":")[0];
        String port = address.split(":")[1];
        return new InetSocketAddress(host, Integer.parseInt(port));
    }
}
