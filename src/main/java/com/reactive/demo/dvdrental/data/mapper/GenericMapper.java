package com.reactive.demo.dvdrental.data.mapper;

import com.reactive.demo.dvdrental.api.model.*;
import com.reactive.demo.dvdrental.data.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GenericMapper {

    GenericMapper INSTANCE = Mappers.getMapper(GenericMapper.class);

    StaffModel convert(Staff staff);

    Staff convertToStaff(StaffCoreModel staffCoreModel);

    ActorModel convert(Actor actor);

    AddressModel convert(Address address);

    CityModel convert(City city);

    CountryModel convert(Country country);

    CustomerModel convert(Customer customer);

    FilmModel convert(Film film);

    FilmCategoryModel convert(FilmCategory filmCategory);

    InventoryModel convert(Inventory inventory);

    LanguageModel convert(Language language);

    PaymentModel convert(Payment payment);

    RentalModel convert(Rental rental);

    StoreModel convert(Store store);

}