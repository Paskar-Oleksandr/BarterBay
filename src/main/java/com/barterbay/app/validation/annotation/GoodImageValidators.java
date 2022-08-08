package com.barterbay.app.validation.annotation;

import com.barterbay.app.validation.constraint.GoodImageConstraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GoodImageConstraint.class)
@Documented
public @interface GoodImageValidators {
  String message() default "Good have issues with photo";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
