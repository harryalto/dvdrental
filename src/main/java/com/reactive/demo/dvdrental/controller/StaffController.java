package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.api.model.StaffModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequestMapping(value = "/v1/staff")
@Tag(name = "Staff", description = "REST API for Staff")
public interface StaffController {
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Staff", description = "Get Staff By Id")
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
    Mono<StaffModel> findById(@PathVariable Long id);


    @PostMapping(  produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Create Staff", description = "Create Staff")
    @Parameter(name = "Staff Id.", required = true)
    @ApiResponses
            (
                    value =
                            {
                                    @ApiResponse(responseCode = "201", description = "Created."),
                                    @ApiResponse(responseCode = "200", description = "OK."),
                                    @ApiResponse(responseCode = "400", description = "Invalid Data provided.")
                            }
            )
    Mono<ResponseEntity<StaffModel>> create(@Valid @RequestBody StaffCoreModel staffCoreModelRequest);



}
