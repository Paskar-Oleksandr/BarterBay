package com.barterbay.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
  @UtilityClass
  public class UserConstants {
    public static final String USERNAME_PATTERN = "^[a-zA-z ‘`'\\-]+$";

  /* Minimum eight characters, max 100 characters, at least one uppercase letter, one lowercase letter,
   one number and one special character like ! @ # & ( ) */

    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,100}$";
  }
}
