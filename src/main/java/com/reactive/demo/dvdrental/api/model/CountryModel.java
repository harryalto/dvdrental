package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Country Model")
@Data
public class CountryModel {
    private long countryId;
    private String country;
}
