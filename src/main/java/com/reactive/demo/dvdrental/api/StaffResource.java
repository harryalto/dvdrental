package com.reactive.demo.dvdrental.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema( title = "Staff Resource")
@Data
public class StaffResource {
    @ApiModelProperty(
            value = "Staff Id",
            name = "staffId",
            dataType = "String",
            example = "12345",
            required = true,
            position = 9)
    private Integer staffId;

    @ApiModelProperty(
            notes = "Staff firstname",
            example = "Joe",
            required = true,
            position = 1)
    private String firstName;

    @ApiModelProperty(notes = "Staff Member Last Name.",
            example = "Doe",
            required = true, position = 2)
    private String lastName;

    @ApiModelProperty(notes = "Staff Member Email",
            example = "Doe", required = true, position = 3)
    private String email;
    @ApiModelProperty(notes = "Staff Member StoreId", example = "12", required = true, position = 4)
    private Integer storeId;

    @ApiModelProperty(notes = "Staff Member Active Status", example = "true", required = true, position = 5)
    private String active;

    @ApiModelProperty(notes = "Staff Member username", example = "joedoe", required = true, position = 6)
    private String username;

    @ApiModelProperty(notes = "Staff Member Picture", example = "binarydata", required = true, position = 7)
    private String picture;
    @ApiModelProperty(notes = "Staff Member Address Id.", example = "23", required = true, position = 8)
    private Integer addressId;


}
