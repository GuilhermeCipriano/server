package com.cipriano.response;

import lombok.Data;

@Data
public class PaymentResponse {

    private Float value;
    private String transmission_date_time;
    private String nsu;
    private String local_transaction_time;
    private String local_transaction_date;
    private String authorization_identification_response;
    private String response_code;
    private String card_acceptor_identification_code;
    private String nsu_host;


}
