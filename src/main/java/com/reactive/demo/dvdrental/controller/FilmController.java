package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.model.FilmModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.api.request.FilmRequest;
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

@RequestMapping(value = "/v1/films")
@Tag(name = "Films", description = "REST API for films")
public interface FilmController {

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get All Films", description = " Get All films")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK.", content = {@Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = FilmModel.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid Request."),
            @ApiResponse(responseCode = "404", description = "No data found.")
    })
    Flux<FilmModel> findAll(@Parameter(description = "Search for release year") @RequestParam(required = false) String releaseYear);

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get by Film Id", description = "Get Film by film id")
    @Parameter(name = "Film Id", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK.", content = {@Content(schema = @Schema(implementation = FilmModel.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Address Id"),
            @ApiResponse(responseCode = "404", description = "No Data Found")
    })
    Mono<FilmModel> getById(@PathVariable Long id);

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create new film", description = " Create new film")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {@Content(schema = @Schema(implementation = AddressModel.class)
            )}),
            @ApiResponse(responseCode = "200", description = "OK."),
            @ApiResponse(responseCode = "400", description = "Invalid Data provided.")

    })
    Mono<ResponseEntity<FilmModel>> create(@Valid @RequestBody FilmRequest filmRequest);

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a film",
            description = "Delete film by Id.",
            tags = {"Films"},
            responses = {
                    @ApiResponse(description = "Deleted", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    Mono<ResponseEntity<Void>> deleteById(@PathVariable @Parameter(description = "The Id of the film to delete.") Long id);

}
