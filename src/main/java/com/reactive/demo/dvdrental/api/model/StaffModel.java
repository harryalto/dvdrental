package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Schema(title = "Staff Model")
@Data
@SuperBuilder
public class StaffModel extends StaffCoreModel {
    private Long staffId;
}
