package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.data.entity.Address;
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
    public Flux<AddressModel> findAll(String postalCode) {
        HashMap<Long, City> cityMap = new HashMap<>();
        HashMap<Long, Country> countryMap = new HashMap<>();
        Flux<AddressModel> addresses = getAll(postalCode)
                .switchIfEmpty(Mono.error(new DataNotFoundException(0)))
                .flatMap(address -> {
                    AddressModel addressModel = GenericMapper.INSTANCE.addressToAddressModel(address);
                    return Mono.just(addressModel);
                });
        Flux<Tuple2<AddressModel, City>> addressesWithCity =
                getAddressesWithCity(cityMap, addresses);
        return getAddressWithCityAndCountry(countryMap, addressesWithCity)
                .flatMap(tuple -> {
                            AddressModel addressModel = tuple.getT1().getT1();
                            addressModel.setCity(tuple.getT1().getT2().getCity());
                            addressModel.setCountry(tuple.getT2().getCountry());
                            return Mono.just(addressModel);
                        }
                );
    }

    private Flux<Tuple2<Tuple2<AddressModel, City>, Country>> getAddressWithCityAndCountry(
            HashMap<Long, Country> countryMap,
            Flux<Tuple2<AddressModel, City>> addressesWithCity) {
        return addressesWithCity.flatMap(
                tuple ->
                        Mono.just(tuple)
                                .zipWith(
                                        Mono.justOrEmpty(countryMap.get(tuple.getT2().getCountryId()))
                                                .switchIfEmpty(Mono.defer(() -> countryRepository.findById(tuple.getT2().getCountryId())))
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
    }

    private Flux<Tuple2<AddressModel, City>> getAddressesWithCity(
            HashMap<Long, City> cityMap,
            Flux<AddressModel> addresses) {
        return addresses.flatMap(address ->
                Mono.just(address)
                        .zipWith(
                                Mono.justOrEmpty(cityMap.containsKey(address.getCityId()) ? cityMap.get(address.getCityId()) : null)
                                        .switchIfEmpty( Mono.defer( () ->
                                                cityRepository.findById(address.getCityId()).doOnNext(data ->
                                                {
                                                    System.out.println("City Map Size" + cityMap.size());
                                                    if (!cityMap.containsKey(data.getCityId())) {
                                                        cityMap.put(data.getCityId(), data);
                                                    }
                                                })
                                        )
                        ))
        );
    }

    private Flux<Address> getAll(String postalCode) {
        if (postalCode != null) {
            return addressRepository.findByPostalCode(postalCode);
        } else
            return addressRepository
                    .findAll();
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
                        Mono.just(addressModelLongTuple2.getT1())
                                .zipWith(countryRepository.findById(addressModelLongTuple2.getT2())))
                .map(tuple -> {
                            AddressModel result = tuple.getT1();
                            result.setCountry(tuple.getT2().getCountry());
                            return result;
                        }
                );
    }

    @Override
    public Mono<Boolean> deleteById(Long id) {
        return Mono.just(id)
                .flatMap(inputId ->
                        addressRepository.findById(inputId)
                                .map(existingActor -> Pair.of(true, existingActor))
                                .defaultIfEmpty(Pair.of(false, null)))
                .flatMap(pair -> {
                    if (pair.getFirst()) {
                        return addressRepository.delete(pair.getSecond()).then(Mono.just(Boolean.TRUE));
                    } else
                        return Mono.just(Boolean.FALSE);
                });
    }

    @Override
    public Mono<Pair<Boolean, Address>> save(AddressRequest addressRequest) {

        return getOrCreateCountry(addressRequest.getCountry())
                .flatMap(country -> getOrCreateCity(country.getCountryId(), addressRequest.getCity()))
                .flatMap(city -> {
                    Address addressEntity = GenericMapper.INSTANCE.addressRequestToAddress(addressRequest);
                    return Mono.just(addressEntity.toBuilder().cityId(city.getCityId()).build());
                }).flatMap(this::getOrCreateAddress);

    }

    /*
     Learnings: When writing this i found one issue was switchIfEmpty was getting eagerly evaluated
     Using Mono.defer() solved the problem. Read this stackoverflow to figure out
     https://stackoverflow.com/questions/54373920/mono-switchifempty-is-always-called

     */
    private Mono<City> getOrCreateCity(final Long countryId, final String cityName) {
        return cityRepository
                .findFirstByCityAndCountryId(cityName, countryId)
                .switchIfEmpty(Mono.defer(() ->
                        cityRepository.save(City.builder().city(cityName).countryId(countryId).build()))
                );
    }

    private Mono<Country> getOrCreateCountry(final String countryName) {
        return countryRepository
                .findFirstByCountry(countryName)
                .switchIfEmpty(
                        Mono.defer(() -> countryRepository.save(Country.builder().country(countryName).build())
                        ));
    }

    private Mono<Pair<Boolean, Address>> getOrCreateAddress(final Address address) {
        return Mono.just(address)
                .flatMap(addressInput ->
                        addressRepository.findFirstByAddressAndAddress2AndPhoneAndCityIdAndDistrictAndPostalCode(
                                        addressInput.getAddress(),
                                        addressInput.getAddress2(),
                                        addressInput.getPhone(),
                                        addressInput.getCityId(),
                                        addressInput.getDistrict(),
                                        addressInput.getPostalCode())
                                .map(existingAddress -> Pair.of(true, existingAddress))
                                .defaultIfEmpty(Pair.of(false, address)))
                .flatMap(tuple -> {
                    if (tuple.getFirst().booleanValue() == false) {
                        return addressRepository.save(tuple.getSecond()).map(savedData -> Pair.of(false, savedData));
                    } else
                        return Mono.just(tuple);

                });
    }
}
