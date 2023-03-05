package com.reactive.demo.dvdrental.controller.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.data.entity.Address;
import com.reactive.demo.dvdrental.service.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class AddressControllerImplTest {
    @InjectMocks
    private AddressControllerImpl addressController;

    @Mock
    private AddressService addressService;

    Address sampleAddress = Address.builder()
            .addressId(1).address("Test").address2("test2")
            .cityId(1).postalCode("123").district("AA").phone("11")
            .build();
    AddressRequest sampleAddressRequest = AddressRequest.builder().address("Test").address2("test2")
            .city("AA").country("AA").postalCode("123").district("AA").phone("11").build();
    AddressModel addressModel = AddressModel.builder().addressId(1).address("Test").address2("test2")
            .cityId(1).city("AA").country("AA").postalCode("123").district("AA").phone("11").build();

    @Test
    @DisplayName("findAll returns all addresses ")
    public void findAll_ReturnFluxAddress_Success() {
        BDDMockito.when(addressService.findAll(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(addressModel));
        StepVerifier.create(addressController.findAll(new String("AA")))
                .expectSubscription()
                .expectNext(new AddressModel[]{addressModel})
                .verifyComplete();
    }


    @Test
    @DisplayName("findById returns a Mono with address when it exists")
    public void findById_ReturnMonoAddress_Success() {
        BDDMockito.when(addressService.getById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(addressModel));
        StepVerifier.create(addressController.getById(2L))
                .expectSubscription()
                .expectNext(addressModel)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns empty with address when it doesnt exists")
    public void findById_ReturnMonoEmpty_Failure() {
        BDDMockito.when(addressService.getById(2L))
                .thenReturn(Mono.empty());
        StepVerifier.create(addressController.getById(2L))
                .expectSubscription()
                .expectNextCount(0).
                verifyComplete();
    }

    @Test
    @DisplayName("Create new address ")
    public void createNew_Success() {
        BDDMockito.when(addressService.save(sampleAddressRequest))
                .thenReturn(Mono.just(Pair.of(true, sampleAddress)));
        StepVerifier.create(addressController.create(sampleAddressRequest))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }

    @Test
    @DisplayName("Address getById Return existing address")
    public void createReturnExisting_Success() {
        BDDMockito.when(addressService.save(sampleAddressRequest))
                .thenReturn(Mono.just(Pair.of(false, sampleAddress)));
        StepVerifier.create(addressController.create(sampleAddressRequest))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }


    @Test
    @DisplayName("Address deleteById returns a Mono void when it exists")
    public void deleteById_ReturnHTTP204OK_Success() {
        BDDMockito.when(addressService.deleteById(2L))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(addressController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }


    @Test
    @DisplayName("deleteById returns a Mono void when it doesnt exit")
    public void deleteById_ReturnMonoVoid_Success() {
        BDDMockito.when(addressService.deleteById(2L))
                .thenReturn(Mono.just(Boolean.FALSE));
        StepVerifier.create(addressController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.notFound().build())
                .verifyComplete();
    }


}