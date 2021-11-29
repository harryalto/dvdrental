package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Film Category Model")
@Data
public class FilmCategoryModel {
    private long filmId;
    private long categoryId;
}
