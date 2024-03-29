package com.barterbay.app.mapper;

import com.barterbay.app.domain.User;
import com.barterbay.app.domain.dto.user.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDTO userEntityToUserDTO(User user);
}
