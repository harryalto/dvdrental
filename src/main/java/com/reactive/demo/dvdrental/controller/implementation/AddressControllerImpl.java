package com.reactive.demo.dvdrental.controller.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.controller.AddressController;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public Flux<AddressModel> findAll(String postalCode) {
        return addressService.findAll(postalCode);
    }

    @Override
    public Mono<AddressModel> getById(Long id) {
        return addressService.getById(id);
    }

    @Override
    public Mono<ResponseEntity<AddressModel>> create(AddressRequest addressRequest) {
        return addressService.save(addressRequest)
                .map(data -> {
                    AddressModel addressModel = GenericMapper.INSTANCE.addressToAddressModel(data.getSecond());
                    System.out.println(addressModel.toString());
                    return Pair.of(data.getFirst(), addressModel);
                })
                .map(pairData -> {
                    if (pairData.getFirst()) {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.CREATED);
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteById(Long id) {
        return addressService.deleteById(id).flatMap(result -> {
            if (result.booleanValue() == Boolean.TRUE) {
                return Mono.just(ResponseEntity.noContent().build());
            } else
                return Mono.just(ResponseEntity.notFound().build());
        });
    }
}
