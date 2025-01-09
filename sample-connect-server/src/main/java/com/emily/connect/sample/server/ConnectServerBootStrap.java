package com.emily.connect.sample.server;


import com.emily.connect.server.handler.ServerBusinessHandler;
import com.emily.connect.server.ServerConnection;
import com.emily.connect.server.ServerProperties;

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
