package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FilmCategory {

    private long filmId;
    private long categoryId;
    private java.sql.Timestamp lastUpdate;

}
