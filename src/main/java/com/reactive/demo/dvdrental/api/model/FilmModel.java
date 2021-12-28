package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(title = "Film Model")
@Data
public class FilmModel {

    private long filmId;
    private String title;
    private String description;
    private String releaseYear;
    private String language;
    private long rentalDuration;
    private String rentalRate;
    private long length;
    private String replacementCost;
    private String rating;
    private ArrayList<String> specialFeaturesList;
    private String fulltext;
    private String category;
    List<ActorModel> actors;
}
