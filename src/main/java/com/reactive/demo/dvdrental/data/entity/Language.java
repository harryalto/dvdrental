package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Language {

    @Id
    private long languageId;
    private String name;
    private java.sql.Timestamp lastUpdate;

}
