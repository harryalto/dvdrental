package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Payment {

    private long paymentId;
    private long customerId;
    private long staffId;
    private long rentalId;
    private String amount;
    private java.sql.Timestamp paymentDate;

}
