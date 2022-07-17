package com.barterbay.app.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class TypeCast {

  public static <T> Optional<T> tryCast(Object input, Class<T> type) {
    if (input.getClass().isAssignableFrom(type)) {
      return Optional.of(type.cast(input));
    }
    return Optional.empty();
  }
}
