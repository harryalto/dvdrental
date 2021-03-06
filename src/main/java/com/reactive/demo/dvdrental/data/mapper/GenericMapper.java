package com.reactive.demo.dvdrental.data.mapper;

import com.reactive.demo.dvdrental.api.model.*;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.api.request.FilmRequest;
import com.reactive.demo.dvdrental.data.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
public interface GenericMapper {

    GenericMapper INSTANCE = Mappers.getMapper(GenericMapper.class);

    StaffModel convert(Staff staff);

    Staff convertToStaff(StaffCoreModel staffCoreModel);

    ActorModel actorToActorModel(Actor actor);

    AddressModel addressToAddressModel(Address address);

    CityModel cityToCityModel(City city);

    CountryModel countryToCountryModel(Country country);

    CustomerModel customerToCustomerModel(Customer customer);

    FilmModel filmToFilmModel(Film film);

    FilmCategoryModel toFilmCategoryModel(FilmCategory filmCategory);

    InventoryModel toInventoryModel(Inventory inventory);

    LanguageModel toLanguageModel(Language language);

    PaymentModel toPaymentModel(Payment payment);

    RentalModel toRentalModel(Rental rental);

    StoreModel toStoreModel(Store store);

    Actor actorModelToActor(ActorModel actorModel);

    Actor actorRequestToActor(ActorRequest actorModel);

    Address addressRequestToAddress(AddressRequest addressRequest);

    @Mapping(
            source = "specialFeaturesList",
            target = "specialFeatures",
            qualifiedByName = "getSpecialFeatures")
    Film filmRequestToFilm(FilmRequest filmRequest);

    @Named("getSpecialFeatures")
    default String[] getSpecialFeatures(ArrayList<String> specialFeatures) {
        return specialFeatures.stream().toArray(String[]::new);
        // return specialFeatures.toArray(new String[0]);
        // return specialFeatures.stream().collect(Collectors.joining(",", "{", "}"));
    }
}