package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Inventory Model")
@Data
public class InventoryModel {
    private long inventoryId;
    private long filmId;
    private long storeId;

}
