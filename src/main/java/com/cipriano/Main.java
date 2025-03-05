package com.cipriano;

import com.cipriano.factory.CreditCardPaymentFactory;
import com.cipriano.request.PaymentRequest;
import com.cipriano.response.PaymentResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException, ISOException {
        final Logger LOGGER = LogManager.getLogger(Main.class);
        LOGGER.info("Starting Payment Service");
        ServerSocket serverSocket = new ServerSocket(8081);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LOGGER.info("Accepted connection from " + clientSocket.getInetAddress().getHostName());
            CreditCardPaymentFactory creditCardPaymentFactory = new CreditCardPaymentFactory();
            PaymentRequest paymentRequest = creditCardPaymentFactory.getPayment(IOUtils.toByteArray(clientSocket.getInputStream()));
            clientSocket.getInputStream().close();
            PaymentResponse paymentResponse = creditCardPaymentFactory.processTransaction(paymentRequest);
            byte[] response = creditCardPaymentFactory.packagePaymentResponse(paymentResponse);
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(response);
            outputStream.flush();
            outputStream.close();
            System.out.println(paymentRequest);

        }

    }



}