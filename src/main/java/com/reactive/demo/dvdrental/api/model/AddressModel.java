package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Address Model")
@Data
public class AddressModel {
    private long addressId;
    private String address;
    private String address2;
    private String district;
    private long cityId;
    private String postalCode;
    private String phone;
}
