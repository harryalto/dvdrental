package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;


@Table
@Value
@Builder
public class Staff {
    @Id
    int staffId;
    String firstName;
    String lastName;
    long addressId;
    String email;
    long storeId;
    String active;
    String username;
    String password;
    @LastModifiedDate
    java.sql.Timestamp lastUpdate;
    String picture;
}
