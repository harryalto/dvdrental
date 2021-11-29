package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Customer Model")
@Data
public class CustomerModel {

    private long customerId;
    private long storeId;
    private String firstName;
    private String lastName;
    private String email;
    private long addressId;
    private String activebool;
    private long active;
}
