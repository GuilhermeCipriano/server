package com.cipriano.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String external_id;
    private Float value;
    private String card_number;
    private Integer installments;
    private String cvv;
    private Integer exp_month;
    private String exp_year;
    private String holder_name;
}


