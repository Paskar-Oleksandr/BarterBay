package com.barterbay.app.domain.dto;

import com.barterbay.app.validation.annotation.UserUniqueEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Please enter a valid email address")
  @UserUniqueEmail
  private String email;

  @NotBlank(message = "First name can not be empty")
  private String firstName;

  // TODO complete this DTO
}
