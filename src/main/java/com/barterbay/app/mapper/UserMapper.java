package com.barterbay.app.mapper;

import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.user.UserDTO;
import com.barterbay.app.domain.dto.user.UserRegistrationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User userDTOToUserEntity(UserDTO userDTO);

  UserDTO userEntityToUserDTO(User user);

  User userRegistrationDTOToUserEntity(UserRegistrationDTO userRegistrationDTO);

  UserRegistrationDTO userEntityToUserRegistrationDTO(User user);
}
