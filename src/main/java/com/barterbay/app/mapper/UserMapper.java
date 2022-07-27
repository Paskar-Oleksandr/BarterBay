package com.barterbay.app.mapper;

import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  User userDTOToUserEntity(UserDTO userDTO);

  UserDTO userEntityToUserDTO(User user);
}
