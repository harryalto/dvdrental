package com.reactive.demo.dvdrental.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffCoreModel {
    String firstName;
    String lastName;
    String email;
    Long storeId;
    boolean active;
    String username;
    String picture;
    Long addressId;
}
