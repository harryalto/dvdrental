package com.reactive.demo.dvdrental.data.entity;


import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder(toBuilder = true)
public class Address {

    @Id
    private long addressId;
    private String address;
    private String address2;
    private String district;
    private long cityId;
    private String postalCode;
    private String phone;
    private java.sql.Timestamp lastUpdate;

}
