package com.barterbay.app.exception;

import lombok.Getter;

@Getter
public class SendEmailException extends RuntimeException {
  private final String message;

  public SendEmailException(String message) {
    super(message);
    this.message = message;
  }
}
