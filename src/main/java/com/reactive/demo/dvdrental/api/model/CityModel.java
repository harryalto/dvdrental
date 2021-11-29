package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "City Model")
@Data
public class CityModel {

    private long cityId;
    private String city;
    private long countryId;

}
