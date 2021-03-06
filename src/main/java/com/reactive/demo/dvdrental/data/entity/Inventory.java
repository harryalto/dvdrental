package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Inventory {

  @Id
  private long inventoryId;
  private long filmId;
  private long storeId;
  private java.sql.Timestamp lastUpdate;

}
