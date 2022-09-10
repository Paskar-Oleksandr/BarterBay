package com.barterbay.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
  public static final String SLASH_DELIMITER = "/";
  public static final String START_COMMAND = "/start";
  public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,100}$";
}
