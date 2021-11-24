package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Store {

    private long storeId;
    private long managerStaffId;
    private long addressId;
    private java.sql.Timestamp lastUpdate;

}
