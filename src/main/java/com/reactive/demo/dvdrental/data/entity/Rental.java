package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Rental {

  @Id
  private long rentalId;
  private java.sql.Timestamp rentalDate;
  private long inventoryId;
  private long customerId;
  private java.sql.Timestamp returnDate;
  private long staffId;
  private java.sql.Timestamp lastUpdate;

}
