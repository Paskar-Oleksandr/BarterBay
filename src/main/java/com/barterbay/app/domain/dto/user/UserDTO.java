package com.barterbay.app.domain.dto.user;

import com.barterbay.app.validation.annotation.UserUniqueEmail;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserDTO {

  private Long id;

  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Please enter a valid email address")
  @UserUniqueEmail
  private String email;

  @NotBlank(message = "First name cannot be empty")
  private String firstName;

  private String lastName;

  private String password;

}
