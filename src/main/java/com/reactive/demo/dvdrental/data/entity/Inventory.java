package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Inventory {

    private long inventoryId;
    private long filmId;
    private long storeId;
    private java.sql.Timestamp lastUpdate;

}
