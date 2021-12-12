package com.reactive.demo.dvdrental.controller.implementation;

import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.controller.AddressController;
import com.reactive.demo.dvdrental.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class AddressControllerImpl implements AddressController {
    private final AddressService addressService;

    public AddressControllerImpl(final AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public Flux<AddressModel> findAll() {
        return addressService.findAll();
    }

    @Override
    public Mono<AddressModel> getById(Long id) {
        return addressService.getById(id);
    }

    @Override
    public Mono<AddressModel> create() {
        return null;
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteById(Long id) {
        return null;
    }
}
