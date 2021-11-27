package com.reactive.demo.dvdrental.data.mapper;

import com.reactive.demo.dvdrental.api.StaffResource;
import com.reactive.demo.dvdrental.data.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GenericMapper {

  //  StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);

    StaffResource staffToStaffResource(Staff staff);
}