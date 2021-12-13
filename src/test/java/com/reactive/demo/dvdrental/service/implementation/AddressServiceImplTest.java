package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.data.entity.Address;
import com.reactive.demo.dvdrental.data.entity.City;
import com.reactive.demo.dvdrental.data.entity.Country;
import com.reactive.demo.dvdrental.data.repository.AddressRepository;
import com.reactive.demo.dvdrental.data.repository.CityRepository;
import com.reactive.demo.dvdrental.data.repository.CountryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

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

    @Test
    @DisplayName("Get All Addresses")
    public void testFindAll_Successful() {
        BDDMockito.when(addressRepository.findAll())
                .thenReturn(Flux.just(sampleAddress));
        BDDMockito.when(cityRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCity));
        BDDMockito.when(countryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCountry));

        StepVerifier.create(addressService.findAll(null))
                .expectSubscription()
                .expectNext(addressModel)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get All Addresses with PostalCode")
    public void testFindAllWithQuery_Successful() {
        BDDMockito.when(addressRepository.findByPostalCode(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(sampleAddress));
        BDDMockito.when(cityRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCity));
        BDDMockito.when(countryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCountry));
        BDDMockito.verify(addressRepository, BDDMockito.times(0)).findAll();

        StepVerifier.create(addressService.findAll("test"))
                .expectSubscription()
                .expectNext(addressModel)
                .verifyComplete();
    }

    @Test
    @DisplayName("Get Address by Id ")
    public void testGetAddressById_Successful() {
        BDDMockito.when(addressRepository.findFirstByAddressId(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleAddress));
        BDDMockito.when(cityRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCity));
        BDDMockito.when(countryRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(sampleCountry));
        BDDMockito.verify(addressRepository, BDDMockito.times(0)).findAll();

        StepVerifier.create(addressService.getById(1L))
                .expectSubscription()
                .expectNext(addressModel)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete Address by ID")
    public void testDeleteByIdWhenPresent_Successful() {
        BDDMockito.when(addressRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Mono.just(sampleAddress));
        BDDMockito.when(addressRepository.delete(ArgumentMatchers.any(Address.class))).thenReturn(Mono.empty());

        StepVerifier.create(addressService.deleteById(1L))
                .expectSubscription()
                .expectNext(Boolean.TRUE)
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete by ID when Id doesnt exist")
    public void testDeleteByIdWhenIdDoesntExist_Successful() {
        BDDMockito.when(addressRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Mono.empty());
        StepVerifier.create(addressService.deleteById(1L))
                .expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

    @Test
    @DisplayName("Create a new address when city and country doesnt exist")
    public void testCreateNew_Successful() {
        BDDMockito.when(countryRepository.findFirstByCountry(ArgumentMatchers.anyString())).thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(countryRepository.save(ArgumentMatchers.any(Country.class))).thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(cityRepository.findFirstByCityAndCountryId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(sampleCity));
        BDDMockito.when(cityRepository.save(ArgumentMatchers.any(City.class))).thenReturn(Mono.just(sampleCity));
        BDDMockito.when(addressRepository.findFirstByAddressAndAddress2AndPhoneAndCityIdAndDistrictAndPostalCode(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Mono.empty());
        BDDMockito.when(addressRepository.save(ArgumentMatchers.any(Address.class))).thenReturn(Mono.just(sampleAddress));

        StepVerifier.create(addressService.save(sampleAddressRequest))
                .expectSubscription()
                .expectNext(Pair.of(Boolean.FALSE, sampleAddress))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return address when exist")
    public void testCreateNewWhenExists_Successful() {
        BDDMockito.when(countryRepository.findFirstByCountry(ArgumentMatchers.anyString())).thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(countryRepository.save(ArgumentMatchers.any(Country.class))).thenReturn(Mono.just(sampleCountry));
        BDDMockito.when(cityRepository.findFirstByCityAndCountryId(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong())).thenReturn(Mono.just(sampleCity));
        BDDMockito.when(cityRepository.save(ArgumentMatchers.any(City.class))).thenReturn(Mono.just(sampleCity));
        BDDMockito.when(addressRepository.findFirstByAddressAndAddress2AndPhoneAndCityIdAndDistrictAndPostalCode(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyLong(),
                ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString())).thenReturn(Mono.just(sampleAddress));
        BDDMockito.when(addressRepository.save(ArgumentMatchers.any(Address.class))).thenReturn(Mono.just(sampleAddress));
        BDDMockito.verify(addressRepository, BDDMockito.times(0)).save(ArgumentMatchers.any(Address.class));

        StepVerifier.create(addressService.save(sampleAddressRequest))
                .expectSubscription()
                .expectNext(Pair.of(Boolean.TRUE, sampleAddress))
                .verifyComplete();
    }
}