package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Value
@Builder
public class Film {
    @Id
    private long filmId;
    private String title;
    private String description;
    private String releaseYear;
    private long languageId;
    private long rentalDuration;
    private String rentalRate;
    private long length;
    private String replacementCost;
    @Column("rating")
    private Rating rating;
    private java.sql.Timestamp lastUpdate;
    private String specialFeatures;
}

