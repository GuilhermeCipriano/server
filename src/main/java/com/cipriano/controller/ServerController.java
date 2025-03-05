package com.cipriano.controller;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController {

    public ServerController() {
    }


    public byte[] receivePaymentTransaction(ServerSocket serverSocket, Socket clientSocket) throws IOException {
        byte[] paymenteTransaction = new byte[0];
        if(clientSocket.isConnected()) {
            System.out.println("Accepted connection from " + clientSocket.getLocalSocketAddress());
            paymenteTransaction = IOUtils.toByteArray(clientSocket.getInputStream());
            System.out.println("Received data: " + paymenteTransaction.toString());
        }

        return paymenteTransaction;
    }

    public void sendPaymentTransactionResponse(Socket clientSocket, byte[] paymentTransaction) throws IOException {
        clientSocket.getOutputStream().write(paymentTransaction);
    }

}
