package com.cipriano.factory;

import com.cipriano.request.PaymentRequest;
import org.jpos.iso.ISOException;

public interface IPaymentFactory {

    byte[] createPayment(PaymentRequest payment) throws ISOException;
}
