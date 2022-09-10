package com.barterbay.app.domain.dto.user;

import com.barterbay.app.util.Constants;
import com.barterbay.app.validation.annotation.UserUniqueEmail;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserRegistrationDTO {

  @NotBlank(message = "Dear User, this email is already in use, please, choose another email")
  @Email(message = "Dear User, please, provide proper email address")
  @UserUniqueEmail(message = "Such email already exist")
  private String email;

  @NotBlank(message = "Password cannot be empty")
  @Pattern(regexp = Constants.PASSWORD_PATTERN)
  private String password;
}
