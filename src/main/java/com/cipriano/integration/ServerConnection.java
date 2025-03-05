package com.cipriano.integration;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerConnection {

    private static final String HOST = "localhost";
    private static final int PORT = 8081;
    private static ServerConnection instance;
    private ServerSocket socket;

    private ServerConnection() {
        // Private constructor to prevent external instantiation
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            synchronized (ServerConnection.class) {
                if (instance == null) {
                    instance = new ServerConnection();
                }
            }
        }
        return instance;
    }

    public ServerSocket getSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            synchronized (this) {
                if (socket == null || socket.isClosed()) {
                    ServerSocket socket = new ServerSocket(PORT);
                    this.socket = socket;
                }
            }
        }
        return socket;
    }

    public void closeSocket() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
