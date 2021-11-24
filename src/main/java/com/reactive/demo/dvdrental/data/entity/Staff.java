package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
@Builder
public class Staff {
    @Id
    private long staffId;
    private String firstName;
    private String lastName;
    private long addressId;
    private String email;
    private long storeId;
    private String active;
    private String username;
    private String password;
    private java.sql.Timestamp lastUpdate;
    private String picture;

}
