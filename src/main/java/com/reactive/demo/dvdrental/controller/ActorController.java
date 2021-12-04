package com.reactive.demo.dvdrental.controller;


import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequestMapping(value = "/v1/actor")
@Tag(name = "Actor", description = "REST API for Actor")
public interface ActorController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get All Actors", description = "Get all Actors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK.", content = {@Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ActorModel.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid Staff Id provided."),
            @ApiResponse(responseCode = "404", description = "Staff not found.")
    })
    Flux<ActorModel> findAll();

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Actor Details", description = "Get Actor By Id")
    @Parameter(name = "Actor Id.", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "400", description = "Invalid Staff Id provided."),
            @ApiResponse(responseCode = "404", description = "Staff not found.")
    })
    Mono<ActorModel> findById(@PathVariable Long id);


    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create Actor", description = "Create Actor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created."),
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "400", description = "Invalid Data provided.")
    })
    Mono<ResponseEntity<ActorModel>> create(@Valid @RequestBody ActorRequest actorRequest);

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete an Actpr",
            description = "Deletes an Actor by their Id.",
            tags = {"Actor"},
            responses = {
                    @ApiResponse(description = "Deleted", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    Mono<ResponseEntity<Void>> deleteById(@PathVariable @Parameter(description = "The Id of the person to delete.") Long id);

}
