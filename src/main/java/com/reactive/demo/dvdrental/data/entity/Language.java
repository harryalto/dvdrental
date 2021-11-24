package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Language {

    private long languageId;
    private String name;
    private java.sql.Timestamp lastUpdate;

}
