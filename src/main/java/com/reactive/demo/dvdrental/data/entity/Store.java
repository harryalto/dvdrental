package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Value
@Builder
@Table
public class Store {

    @Id
    private long storeId;
    private long managerStaffId;
    private long addressId;
    private java.sql.Timestamp lastUpdate;

}
