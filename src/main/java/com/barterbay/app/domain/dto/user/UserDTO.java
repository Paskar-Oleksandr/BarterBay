package com.barterbay.app.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {

  @NotBlank(message = "First name can not be empty")
  private String firstName;

  private String lastName;

  // TODO complete this DTO
  //Regex
}
