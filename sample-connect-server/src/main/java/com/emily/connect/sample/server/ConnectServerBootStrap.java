package com.emily.connect.sample.server;


import com.emily.connect.server.ServerConnection;
import com.emily.connect.server.ServerProperties;
import com.emily.connect.server.handler.ServerBusinessHandler;

/**
 * @author Emily
 */
public class ConnectServerBootStrap {
    public static void main(String[] args) {
        ServerProperties properties = new ServerProperties();
        ServerBusinessHandler handler = new ServerBusinessHandler();
        new ServerConnection(handler, properties).start();
    }

}
