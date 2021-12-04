package com.reactive.demo.dvdrental.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(title = "Actor Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActorRequest {

    private String firstName;
    private String lastName;
}
