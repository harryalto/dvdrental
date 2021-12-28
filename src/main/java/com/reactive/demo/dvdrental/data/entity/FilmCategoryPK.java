package com.reactive.demo.dvdrental.data.entity;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class FilmCategoryPK implements Serializable {
    private long filmId;
    private long categoryId;
}
