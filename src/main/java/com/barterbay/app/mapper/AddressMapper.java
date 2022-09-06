package com.barterbay.app.mapper;

import com.barterbay.app.domain.Address;
import com.barterbay.app.domain.dto.address.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  @Mapping(target = "id", ignore = true)
  Address addressDTOToAddressEntity(AddressDTO addressDTO);



}
