package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.data.entity.City;
import com.reactive.demo.dvdrental.data.entity.Country;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.AddressRepository;
import com.reactive.demo.dvdrental.data.repository.CityRepository;
import com.reactive.demo.dvdrental.data.repository.CountryRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import com.reactive.demo.dvdrental.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.HashMap;

@Service
@Slf4j

public class AddressServiceImpl implements AddressService {
    private AddressRepository addressRepository;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;

    public AddressServiceImpl(final AddressRepository addressRepository,
                              final CountryRepository countryRepository,
                              final CityRepository cityRepository) {
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public Flux<AddressModel> findAll() {
        HashMap<Long, City> cityMap = new HashMap<>();
        HashMap<Long, Country> countryMap = new HashMap<>();
        Flux<AddressModel> addresses = addressRepository
                .findAll()
                .switchIfEmpty(Mono.error(new DataNotFoundException(0)))
                .flatMap(address -> {
                    AddressModel addressModel = GenericMapper.INSTANCE.addressToAddressModel(address);
                    return Mono.just(addressModel);
                });
        Flux<Tuple2<AddressModel, City>> addressesWithCity =
                addresses.flatMap(address ->
                        Mono.just(address)
                                .zipWith(
                                        Mono.justOrEmpty(cityMap.containsKey(address.getCityId()) ? cityMap.get(address.getCityId()) : null)
                                                .switchIfEmpty(
                                                        cityRepository.findById(address.getCityId()).doOnNext(data ->
                                                        {
                                                            System.out.println("City Map Size" + cityMap.size());
                                                            if (!cityMap.containsKey(data.getCityId())) {
                                                                cityMap.put(data.getCityId(), data);
                                                            }
                                                        })
                                                )
                                )
                );
        Flux<Tuple2<Tuple2<AddressModel, City>, Country>> addressWithCityAndCountry = addressesWithCity.flatMap(
                tuple ->
                        Mono.just(tuple)
                                .zipWith(
                                        Mono.justOrEmpty(countryMap.get(tuple.getT2().getCountryId()))
                                                .switchIfEmpty(countryRepository.findById(tuple.getT2().getCountryId()))
                                                .doOnNext(data ->
                                                        {
                                                            System.out.println("Country Map Size" + countryMap.size());
                                                            if (!countryMap.containsKey(data.getCountryId())) {
                                                                countryMap.put(data.getCountryId(), data);
                                                            }
                                                        }
                                                )
                                )
        );
        return addressWithCityAndCountry.flatMap(tuple -> {
                    AddressModel addressModel = tuple.getT1().getT1();
                    addressModel.setCity(tuple.getT1().getT2().getCity());
                    addressModel.setCountry(tuple.getT2().getCountry());
                    return Mono.just(addressModel);
                }
        );
    }

    @Override
    public Mono<AddressModel> getById(Long id) {
        return addressRepository
                .findFirstByAddressId(id)
                .switchIfEmpty(Mono.error(new DataNotFoundException(0)))
                .map(address -> {
                    AddressModel addressModel = GenericMapper.INSTANCE.addressToAddressModel(address);
                    return addressModel;
                })
                .flatMap(addressModel ->
                        Mono.just(addressModel).zipWith(
                                cityRepository.findById(addressModel.getCityId())))
                .map(tuple -> {
                            AddressModel addressModel = tuple.getT1();
                            addressModel.setCity(tuple.getT2().getCity());
                            return Tuples.of(addressModel, tuple.getT2().getCountryId());
                        }
                )
                .flatMap(addressModelLongTuple2 ->
                        Mono
                                .just(addressModelLongTuple2.getT1())
                                .zipWith(countryRepository.findById(addressModelLongTuple2.getT2())))
                .map(tuple -> {
                            AddressModel result = tuple.getT1();
                            result.setCountry(tuple.getT1().getCountry());
                            return result;
                        }
                );
    }
}
