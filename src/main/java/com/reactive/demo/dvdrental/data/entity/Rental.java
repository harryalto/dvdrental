package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Rental {

    private long rentalId;
    private java.sql.Timestamp rentalDate;
    private long inventoryId;
    private long customerId;
    private java.sql.Timestamp returnDate;
    private long staffId;
    private java.sql.Timestamp lastUpdate;
}
