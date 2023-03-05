package com.reactive.demo.dvdrental.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(title = "Address Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
    @NotNull
    private String address;
    private String address2;
    private String district;
    @NotNull
    private String postalCode;
    private String phone;
    @NotNull
    private String city;
    @NotNull
    private String country;

}
