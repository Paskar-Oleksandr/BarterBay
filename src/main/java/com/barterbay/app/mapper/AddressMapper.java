package com.barterbay.app.mapper;

import com.barterbay.app.domain.Address;
import com.barterbay.app.domain.dto.address.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  Address addressDTOToAddressEntity(AddressDTO addressDTO);
}
