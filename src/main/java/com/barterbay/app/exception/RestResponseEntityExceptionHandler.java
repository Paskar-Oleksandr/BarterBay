package com.barterbay.app.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
  extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value
    = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    final var bodyOfResponse = "This should be application specific";
    return handleExceptionInternal(ex, bodyOfResponse,
      new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<Object> handleAccessDeniedException(
    Exception ex, WebRequest request) {
    return new ResponseEntity<>(
      "Access denied message here", new HttpHeaders(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({SendEmailException.class})
  public ResponseEntity<Object> handleSendEmailException(
    Exception ex, WebRequest request) {
    return new ResponseEntity<>(
      "Error while sending email", new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }
}
