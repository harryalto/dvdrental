package com.reactive.demo.dvdrental.data.entity;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Customer {

  @Id
  private long customerId;
  private long storeId;
  private String firstName;
  private String lastName;
  private String email;
  private long addressId;
  private String activebool;
  private java.sql.Date createDate;
  private java.sql.Timestamp lastUpdate;
  private long active;

}
