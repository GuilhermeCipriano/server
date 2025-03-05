package com.cipriano.factory;

import com.cipriano.request.PaymentRequest;
import com.cipriano.response.PaymentResponse;
import com.cipriano.utils.ISO8583Fields;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOServer;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class CreditCardPaymentFactory implements IPaymentFactory {
    public static final String PAYMENT_MODE_ONE_INSTALLMENT = "003000";
    public static final String PAYMENT_MODE_MANY_INSTALLMENTS = "003001";
    public static final String ONE_INSTALLMENT = "01";
    public static final String ISO_1987 = "ISO1987";
    public static final String MTI_CODE = "0200";
    InputStream inputStream = ISOServer.class.getResourceAsStream("/fields.xml");


    public byte[] packagePaymentResponse(PaymentResponse paymentResponse) throws ISOException {
        InputStream inputStream = ISOServer.class.getResourceAsStream("/fields.xml");
        GenericPackager genericPackager = new GenericPackager(inputStream);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(genericPackager);

        isoMsg.setHeader(ISO_1987.getBytes());
        isoMsg.setMTI(MTI_CODE);


//        isoMsg.set(ISO8583Fields.TRANSACTION_AMOUNT, paymentResponse.getValue().toString());
        isoMsg.set(ISO8583Fields.TRANSACTION_AMOUNT, "23.0");
        isoMsg.set(ISO8583Fields.TRANSMISSION_DATE_AND_TIME, paymentResponse.getTransmission_date_time());
        isoMsg.set(ISO8583Fields.SYSTEM_TRACE_AUDIT_NUMBER, paymentResponse.getNsu());
        isoMsg.set(ISO8583Fields.LOCAL_TRANSACTION_TIME, paymentResponse.getLocal_transaction_time());
        isoMsg.set(ISO8583Fields.LOCAL_TRANSACTION_DATE, paymentResponse.getLocal_transaction_date());
        isoMsg.set(ISO8583Fields.AUTHORIZATION_IDENTIFICATION_RESPONSE, paymentResponse.getAuthorization_identification_response());
        isoMsg.set(ISO8583Fields.RESPONSE_CODE, paymentResponse.getResponse_code());
        isoMsg.set(ISO8583Fields.CARD_ACCEPTOR_IDENTIFICATION_CODE, paymentResponse.getCard_acceptor_identification_code());
        isoMsg.set(ISO8583Fields.RESERVED_PRIVATE_USE_127, paymentResponse.getNsu_host());
        isoMsg.dump(System.out, "");

        return isoMsg.pack();
    }

    public PaymentRequest getPayment(byte[] bytes) throws ISOException {
        PaymentRequest paymentRequest = new PaymentRequest();
        try {
            GenericPackager genericPackager = new GenericPackager(inputStream);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(genericPackager);

            isoMsg.unpack(bytes);

            System.out.println("Received ISO message:");
            isoMsg.dump(System.out, "");

            // Access specific fields:
            String mti = isoMsg.getMTI();
//            String header = isoMsg.getHeader();

            String pan = isoMsg.getString(2); // Primary Account Number

            System.out.println("MTI: " + mti);
            System.out.println("PAN: " + pan);

            paymentRequest.setCard_number(isoMsg.getString(ISO8583Fields.PRIMARY_ACCOUNT_NUMBER));
            paymentRequest.setInstallments(2);
            paymentRequest.setPayment_method(isoMsg.getString(ISO8583Fields.PROCESSING_CODE));
            paymentRequest.setValue(Float.valueOf(isoMsg.getString(ISO8583Fields.TRANSACTION_AMOUNT)));
            paymentRequest.setTransmission_date_time(isoMsg.getString(ISO8583Fields.TRANSMISSION_DATE_AND_TIME));
            paymentRequest.setNsu(isoMsg.getString(ISO8583Fields.SYSTEM_TRACE_AUDIT_NUMBER));
            paymentRequest.setLocal_transaction_time(isoMsg.getString(ISO8583Fields.LOCAL_TRANSACTION_TIME));
            paymentRequest.setLocal_transaction_date(isoMsg.getString(ISO8583Fields.LOCAL_TRANSACTION_DATE));
            paymentRequest.setExp_month(isoMsg.getString(ISO8583Fields.EXPIRATION_DATE).substring(1, 3));
            paymentRequest.setExp_year(isoMsg.getString(ISO8583Fields.EXPIRATION_DATE).substring(0, 2));
            paymentRequest.setPoint_of_service_entry_mode(isoMsg.getString(ISO8583Fields.POINT_OF_SERVICE_ENTRY_MODE));
            paymentRequest.setCard_acceptor_identification_code(isoMsg.getString(ISO8583Fields.CARD_ACCEPTOR_IDENTIFICATION_CODE));
            paymentRequest.setCvv(isoMsg.getString(ISO8583Fields.ADDITIONAL_DATA_PRIVATE));

        } catch (Exception e) {
            e.printStackTrace();
        }




        return paymentRequest;
    }

    private String generateUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 6);
    }

    public PaymentResponse processTransaction(PaymentRequest paymentRequest) {
        System.out.println("processing transaction");
        PaymentResponse paymentResponse = new PaymentResponse();

        paymentResponse.setTransmission_date_time(paymentRequest.getTransmission_date_time());
        paymentResponse.setNsu(paymentRequest.getNsu());
        paymentResponse.setLocal_transaction_time(paymentRequest.getLocal_transaction_time());
        paymentResponse.setLocal_transaction_date(paymentRequest.getLocal_transaction_date());
//        paymentResponse.setAuthorization_identification_response(paymentRequest.get);


        paymentResponse.setResponse_code("051");
        if(paymentRequest.getValue() % 2 == 0) {
            paymentResponse.setResponse_code("000");
        }
        paymentResponse.setCard_acceptor_identification_code(paymentRequest.getCard_acceptor_identification_code());
        paymentResponse.setNsu_host("1000");
        paymentResponse.setValue(paymentRequest.getValue());
        return paymentResponse;

    }
}
