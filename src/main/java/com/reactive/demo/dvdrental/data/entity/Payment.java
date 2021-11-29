package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Payment {

  @Id
  private long paymentId;
  private long customerId;
  private long staffId;
  private long rentalId;
  private String amount;
  private java.sql.Timestamp paymentDate;

}
