package com.cipriano;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        while (true) {
            System.out.println("Hello, World!");
            ServerConnection serverConnection = ServerConnection.getInstance();
            ServerSocket connection = serverConnection.getSocket();

            LOGGER.info(serverConnection.getSocket().getInetAddress().getHostAddress());
            LOGGER.info(serverConnection.getSocket().getLocalSocketAddress());

            Socket clientConnection = connection.accept();
            if(clientConnection.isConnected()) {
                processData(clientConnection, serverConnection);
//                clientConnection.close();
            }

        }
    }

    private static void processData(Socket clientConnection, ServerConnection serverConnection) throws IOException {
        System.out.println("connected");
        InputStream inputStream = clientConnection.getInputStream();
        serverConnection.processISOMessage(IOUtils.toByteArray(inputStream));
        clientConnection.getOutputStream().write("Hello, World! It Workd!".getBytes());
        clientConnection.getOutputStream().flush();
    }
}