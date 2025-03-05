package com.cipriano.request;

import com.cipriano.utils.ISO8583Fields;
import lombok.Data;

@Data
public class PaymentRequest {
    private String external_id;
    private Float value;
    private String card_number;
    private Integer installments;
    private String cvv;
    private String exp_month;
    private String exp_year;
    private String holder_name;
    private String local_transaction_time;
    private String local_transaction_date;
    private String transmission_date_time;
    private String point_of_service_entry_mode;
    private String card_acceptor_identification_code;
    private String payment_method;
    private String nsu;

}


