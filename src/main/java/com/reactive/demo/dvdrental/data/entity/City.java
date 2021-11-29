package com.reactive.demo.dvdrental.data.entity;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class City {
  @Id
  private long cityId;
  private String city;
  private long countryId;
  private java.sql.Timestamp lastUpdate;

}
