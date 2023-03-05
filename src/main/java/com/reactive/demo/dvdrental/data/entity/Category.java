package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Category {
    @Id
    private long categoryId;
    private String name;
    private java.sql.Timestamp lastUpdate;
}
