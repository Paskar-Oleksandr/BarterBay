package com.barterbay.app.exception.aws;

public class S3GeneralException extends RuntimeException {
  private String message;
  private Throwable cause;

  public S3GeneralException(String message, Throwable cause) {
    super(message, cause);
    this.message = message;
    this.cause = cause;
  }

  public S3GeneralException(String message) {
    super(message);
    this.message = message;
  }

  public S3GeneralException(Throwable cause) {
    super(cause);
    this.cause = cause;
  }
}
