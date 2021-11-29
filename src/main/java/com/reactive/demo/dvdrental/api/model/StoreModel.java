package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Store Model")
@Data
public class StoreModel {
    private long storeId;
    private long managerStaffId;
    private long addressId;
}
