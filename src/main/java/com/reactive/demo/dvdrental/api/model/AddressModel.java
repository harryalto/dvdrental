package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(title = "Address Model")
@Data
@Builder
public class AddressModel {
    private long addressId;
    private String address;
    private String address2;
    private String district;
    private long cityId;
    private String country;
    private String city;
    private String postalCode;
    private String phone;
}
