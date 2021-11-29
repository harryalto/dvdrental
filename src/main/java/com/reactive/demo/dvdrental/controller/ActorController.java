package com.reactive.demo.dvdrental.controller;


import com.reactive.demo.dvdrental.api.model.ActorModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(value = "/v1/actor")
@Tag(name = "Actor", description = "REST API for Actor")
public interface ActorController {
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Actor by ID", description = "Get Actor  by ID")
    @Parameter(name = "Staff Id.", required = true)
    @ApiResponses
            (
                    value =
                            {
                                    @ApiResponse(responseCode = "200", description = "OK."),
                                    @ApiResponse(responseCode = "400", description = "Invalid Staff Id provided."),
                                    @ApiResponse(responseCode = "404", description = "Staff not found.")
                            }
            )
    Mono<ActorModel> findById(@PathVariable Long id);


}
