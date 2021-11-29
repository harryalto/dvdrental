package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Actor Resource")
@Data
public class ActorModel {

    private long actorId;
    private String firstName;
    private String lastName;
}
