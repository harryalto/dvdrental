package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Actor {

  @Id
  private long actorId;
  private String firstName;
  private String lastName;
  private java.sql.Timestamp lastUpdate;
}
