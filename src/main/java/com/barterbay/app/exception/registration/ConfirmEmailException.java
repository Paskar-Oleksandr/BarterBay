package com.barterbay.app.exception.registration;

import lombok.Getter;

@Getter
public class ConfirmEmailException extends RuntimeException {
  private final String message;

  public ConfirmEmailException(String message) {
    super(message);
    this.message = message;
  }
}
