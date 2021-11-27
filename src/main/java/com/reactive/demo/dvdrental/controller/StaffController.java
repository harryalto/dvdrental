package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.api.StaffResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(value = "/v1/staff")
public interface StaffController {
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Find Staff by Id.", response = StaffResource.class)
    @ApiParam(value = "Staff Id.", required = true)
    @ApiResponses
            (
                    value =
                            {
                                    @ApiResponse(responseCode = "200", description = "OK."),
                                    @ApiResponse(responseCode = "400", description = "Invalid Staff Id provided."),
                                    @ApiResponse(responseCode = "404", description = "Staff not found.")
                            }
            )
    Mono<StaffResource> findById(@PathVariable Integer id);
}
