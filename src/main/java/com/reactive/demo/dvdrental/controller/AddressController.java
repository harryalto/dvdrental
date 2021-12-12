package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.api.model.AddressModel;
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

@RequestMapping(value = "/v1/addresses")
@Tag(name = "Address", description = "REST API for Address")
public interface AddressController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get All Addresses", description = " Get All addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK.", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = AddressModel.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "No data found.")
    })
    Flux<AddressModel> findAll();

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get by Address Id", description = "Get Address by address id")
    @Parameter(name = "Address Id", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK.", content = {@Content(schema = @Schema(implementation = AddressModel.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Address Id"),
            @ApiResponse(responseCode = "404", description = "No Data Found")
    })
    Mono<AddressModel> getById(@PathVariable Long id);

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create new address", description = " Create new address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(schema = @Schema(implementation = AddressModel.class)
            )}),
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "400", description = "Invalid Data provided.")

    })
    Mono<AddressModel> create();

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete an Address",
            description = "Deletes an Address by their Id.",
            tags = {"Address"},
            responses = {
                    @ApiResponse(description = "Deleted", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    Mono<ResponseEntity<Void>> deleteById(@PathVariable @Parameter(description = "The Id of the person to delete.") Long id);

}
