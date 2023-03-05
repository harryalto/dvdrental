package com.reactive.demo.dvdrental.integration;

import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.controller.AddressController;
import com.reactive.demo.dvdrental.data.entity.Address;
import com.reactive.demo.dvdrental.data.entity.City;
import com.reactive.demo.dvdrental.data.entity.Country;
import com.reactive.demo.dvdrental.data.repository.AddressRepository;
import com.reactive.demo.dvdrental.data.repository.CityRepository;
import com.reactive.demo.dvdrental.data.repository.CountryRepository;
import com.reactive.demo.dvdrental.service.implementation.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = AddressController.class)
@Import(AddressServiceImpl.class)
public class AddressControllerIT {

    @MockBean
    private AddressRepository addressRepositoryMock;

    @MockBean
    private CityRepository cityRepositoryMock;

    @MockBean
    private CountryRepository countryRepositoryMock;

    @Autowired
    private WebTestClient testClient;

    Address sampleAddress = Address.builder()
            .addressId(1).address("Test").address2("test2")
            .cityId(1).postalCode("123").district("AA").phone("11")
            .build();
    AddressRequest sampleAddressRequest = AddressRequest.builder().address("Test").address2("test2")
            .city("AA").country("AA").postalCode("123").district("AA").phone("11").build();
    AddressModel addressModel = AddressModel.builder().addressId(1).address("Test").address2("test2")
            .cityId(1).city("AA").country("AA").postalCode("123").district("AA").phone("11").build();
    City sampleCity = City.builder().city("AA").cityId(1).build();
    Country sampleCountry = Country.builder().country("AA").countryId(1).build();


    @BeforeEach
    public void setUp() {
        BDDMockito.when(addressRepositoryMock.findAll())
                .thenReturn(Flux.just(sampleAddress));
        BDDMockito.when(cityRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCity));
        BDDMockito.when(countryRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(addressRepositoryMock
                .findFirstByAddressId(ArgumentMatchers.anyLong())).thenReturn(Mono.just(sampleAddress));
        BDDMockito.when(addressRepositoryMock
                .save(ArgumentMatchers.any(Address.class))).thenReturn(Mono.just(sampleAddress));

    }

    @Test
    @DisplayName("Get All Address ")
    public void getAllAddress_ReturnFluxOfAddress_Successful() {
        testClient
                .get()
                .uri("/v1/addresses")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].addressId").isEqualTo(addressModel.getAddressId())
                .jsonPath("$.[0].address").isEqualTo(addressModel.getAddress());
    }

    @Test
    @DisplayName("Get Address resource by address Id")
    public void getAddressById_ReturnMonoOfAddress_WhenSuccessful() {
        testClient
                .get()
                .uri("/v1/addresses/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.addressId").isEqualTo(addressModel.getAddressId())
                .jsonPath("$.address").isEqualTo(addressModel.getAddress());
    }

    @Test
    @DisplayName("Create Address Resource")
    public void createAddress_ReturnMono_WhenSuccessful() {
        BDDMockito.when(addressRepositoryMock.findFirstByAddressAndAddress2AndPhoneAndCityIdAndDistrictAndPostalCode(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString()
        )).thenReturn(Mono.empty());
        BDDMockito.when(countryRepositoryMock.findFirstByCountry(ArgumentMatchers.anyString())).thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(cityRepositoryMock.findFirstByCityAndCountryId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(sampleCity));

        testClient.post().uri("/v1/addresses")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(sampleAddressRequest), AddressRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.address").isEqualTo("Test");
    }

    @Test
    @DisplayName("Delete Address Resource by Id")
    public void deleteAddress_ReturnMono_WhenSuccessful() {
        BDDMockito.when(addressRepositoryMock.findById(1L))
                .thenReturn(Mono.just(sampleAddress));
        BDDMockito.when(addressRepositoryMock.delete(
                        ArgumentMatchers.any(Address.class)))
                .thenReturn(Mono.empty());
        testClient
                .delete()
                .uri("/v1/addresses/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
