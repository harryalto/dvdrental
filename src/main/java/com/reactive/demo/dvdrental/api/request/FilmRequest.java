package com.reactive.demo.dvdrental.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(title = "Film Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmRequest {
    @NotNull
    private String title;
    private String description;
    @NotNull
    private String releaseYear;
    @NotNull
    private String language;
    @NotNull
    private String filmCategory;
    private long rentalDuration;
    private String rentalRate;
    private long length;
    private String replacementCost;
    private String rating;
    private String specialFeatures;
    private String fulltext;
    @NotNull
    List<ActorRequest> actors;
}
