package com.reactive.demo.dvdrental.data.entity;

import com.reactive.demo.dvdrental.DictionarySupport;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Value
@Builder(toBuilder = true)
public class Film implements Persistable<Long> {
    @Id
    long filmId;
    String title;
    String description;
    int releaseYear;
    long languageId;
    long rentalDuration;
    BigDecimal rentalRate;
    long length;
    BigDecimal replacementCost;

    @Column("rating")
    MpaaRating rating;
    LocalDateTime lastUpdate;
    String[] specialFeatures;

    @Override
    public Long getId() {
        return filmId;
    }

    @Override
    public boolean isNew() {
        return true;
    }


    public enum MpaaRating implements DictionarySupport {
        G("G"),
        PG("PG"),
        PG13("PG-13") {
            @Override
            public String toString() {
                return "PG-13";
            }
        },
        R("R"),
        NC17("NC-17");

        private final String rating;

        MpaaRating(String rating) {
            this.rating = rating;
            init(rating);
        }
    }
}

