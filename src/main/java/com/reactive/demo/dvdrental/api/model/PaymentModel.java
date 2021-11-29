package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema( title = "Payment Model")
@Data
public class PaymentModel {
    private long paymentId;
    private long customerId;
    private long staffId;
    private long rentalId;
    private String amount;
}
