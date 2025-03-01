package com.cipriano.factory;

import com.cipriano.request.PaymentRequest;
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

    @Override
    public byte[] createPayment(PaymentRequest paymentRequest) throws ISOException {
        GenericPackager genericPackager = new GenericPackager(this.inputStream);

        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(genericPackager);

        isoMsg.setHeader(ISO_1987.getBytes());
        isoMsg.setMTI(MTI_CODE);


        //CreditCard number
        isoMsg.set(ISO8583Fields.PRIMARY_ACCOUNT_NUMBER, paymentRequest.getCard_number());

        //Installments quantity at first glance
        isoMsg.set(ISO8583Fields.EXTENDED_PAYMENT_CODE, ONE_INSTALLMENT);
        isoMsg.set(ISO8583Fields.PROCESSING_CODE, PAYMENT_MODE_ONE_INSTALLMENT);

        if(!paymentRequest.getInstallments().toString().equals(ONE_INSTALLMENT)) {
            isoMsg.set(ISO8583Fields.PROCESSING_CODE, PAYMENT_MODE_MANY_INSTALLMENTS);
            isoMsg.set(ISO8583Fields.EXTENDED_PAYMENT_CODE, paymentRequest.getInstallments().toString());
        }

        isoMsg.set(ISO8583Fields.TRANSACTION_AMOUNT, String.valueOf(paymentRequest.getValue()));
        isoMsg.set(ISO8583Fields.TRANSMISSION_DATE_AND_TIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss")));
        isoMsg.set(ISO8583Fields.SYSTEM_TRACE_AUDIT_NUMBER, this.generateUID());
        isoMsg.set(ISO8583Fields.LOCAL_TRANSACTION_TIME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
        isoMsg.set(ISO8583Fields.LOCAL_TRANSACTION_DATE, LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd")));
        isoMsg.set(ISO8583Fields.EXPIRATION_DATE, paymentRequest.getExp_year() + paymentRequest.getExp_month());
        isoMsg.set(ISO8583Fields.PAN_EXTENDED, "0");

        //Todo: value below must be a variable
        isoMsg.set(ISO8583Fields.POINT_OF_SERVICE_ENTRY_MODE, "210");
        //Todo: value below must be a variable

        isoMsg.set(ISO8583Fields.CARD_ACCEPTOR_IDENTIFICATION_CODE, "123456");
        isoMsg.set(ISO8583Fields.ADDITIONAL_DATA_PRIVATE, paymentRequest.getCvv());
        isoMsg.dump(System.out, "");

        return isoMsg.pack();
    }

    public PaymentRequest getPayment(byte[] bytes) throws ISOException {
        PaymentRequest paymentRequest = new PaymentRequest();
        try {
            GenericPackager genericPackager = new GenericPackager(this.inputStream);

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

            isoMsg.getBytes(ISO8583Fields.PRIMARY_ACCOUNT_NUMBER);
            isoMsg.getBytes(ISO8583Fields.EXTENDED_PAYMENT_CODE);
            isoMsg.getBytes(ISO8583Fields.PROCESSING_CODE);
            isoMsg.getBytes(ISO8583Fields.TRANSACTION_AMOUNT);
            isoMsg.getBytes(ISO8583Fields.TRANSMISSION_DATE_AND_TIME);
            isoMsg.getBytes(ISO8583Fields.SYSTEM_TRACE_AUDIT_NUMBER);
            isoMsg.getBytes(ISO8583Fields.LOCAL_TRANSACTION_TIME);
            isoMsg.getBytes(ISO8583Fields.LOCAL_TRANSACTION_DATE);
            isoMsg.getBytes(ISO8583Fields.EXPIRATION_DATE);
            isoMsg.getBytes(ISO8583Fields.PAN_EXTENDED);
            isoMsg.getBytes(ISO8583Fields.POINT_OF_SERVICE_ENTRY_MODE);
            isoMsg.getBytes(ISO8583Fields.CARD_ACCEPTOR_IDENTIFICATION_CODE);
            isoMsg.getBytes(ISO8583Fields.ADDITIONAL_DATA_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //CreditCard number



        return paymentRequest;
    }

    private String generateUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 6);
    }
}
