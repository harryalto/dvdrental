package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Rental Model")
@Data
public class RentalModel {
    private long rentalId;
    private long inventoryId;
    private long customerId;
    private long staffId;
}
