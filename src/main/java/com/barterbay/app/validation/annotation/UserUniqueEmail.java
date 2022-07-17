package com.barterbay.app.validation.annotation;

import com.barterbay.app.validation.constraint.UserUniqueEmailConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserUniqueEmailConstraint.class)
@Documented
public @interface UserUniqueEmail {

  String message() default "User with this email is already exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
