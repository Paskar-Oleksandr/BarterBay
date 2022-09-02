package com.barterbay.app.exception.registration;

import lombok.Getter;

@Getter
public class OverdueTokenException extends RuntimeException {
  private final String message;
  public OverdueTokenException(String message) {
    super(message);
    this.message = message;
  }
}
