package com.barterbay.app.validation.constraint;

import com.barterbay.app.repository.UserRepository;
import com.barterbay.app.validation.annotation.UserUniqueEmail;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class UserUniqueEmailConstraint implements ConstraintValidator<UserUniqueEmail, String> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return !userRepository.existsByEmail(email);
  }
}
