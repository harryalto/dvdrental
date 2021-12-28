package com.reactive.demo.dvdrental.data.config;

import com.reactive.demo.dvdrental.data.entity.Film;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ReadingConverter
public class FilmReadingConverter implements Converter<Row, Film> {

    /**
     * private long filmId;
     * private String title;
     * private String description;
     * private String releaseYear;
     * private long languageId;
     * private long rentalDuration;
     * private String rentalRate;
     * private long length;
     * private String replacementCost;
     *
     * @Column("rating") private MpaaRating rating;
     * private java.sql.Timestamp lastUpdate;
     * private String specialFeatures;
     */
    @Override
    public Film convert(Row row) {
        return Film.builder()
                .filmId(row.get("film_id", Long.class))
                .title(row.get("title", String.class))
                .description(row.get("description", String.class))
                .releaseYear(row.get("release_year", Integer.class))
                .languageId(row.get("language_id", Long.class))
                .rentalDuration(row.get("rental_duration", Long.class))
                .rentalRate(row.get("rental_rate", BigDecimal.class))
                .length(row.get("length", Long.class))
                .replacementCost(row.get("replacement_cost", BigDecimal.class))
                .rating(row.get("rating", Film.MpaaRating.class))
                .lastUpdate(row.get("last_update", LocalDateTime.class))
                .specialFeatures(row.get("special_features", String[].class))
                .build();
    }
}