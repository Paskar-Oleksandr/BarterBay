package com.barterbay.app.exception.registration;

import lombok.Getter;

@Getter
public class ConfirmationEmailException extends RuntimeException {
  private final String message;

  public ConfirmationEmailException(String message) {
    super(message);
    this.message = message;
  }
}
