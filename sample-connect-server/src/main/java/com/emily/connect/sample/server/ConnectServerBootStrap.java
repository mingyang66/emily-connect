package com.emily.connect.sample.server;


import com.emily.connect.server.ServerConnection;
import com.emily.connect.server.ServerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Emily
 */
@SpringBootApplication
public class ConnectServerBootStrap {

    public static void main(String[] args) {
        SpringApplication.run(ConnectServerBootStrap.class, args);
        ServerProperties properties = new ServerProperties();

        new ServerConnection(properties).start();
    }

}
