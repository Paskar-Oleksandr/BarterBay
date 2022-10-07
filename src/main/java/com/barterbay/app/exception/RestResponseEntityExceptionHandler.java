package com.barterbay.app.exception;

import com.barterbay.app.exception.aws.S3GeneralException;
import com.barterbay.app.exception.registration.ConfirmEmailException;
import com.barterbay.app.exception.registration.OverdueTokenException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException) {
    final var errorMessage = "Access denied";
    return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, errorMessage, accessDeniedException));
  }

  @ExceptionHandler(SendEmailException.class)
  public ResponseEntity<Object> handleSendEmailException(SendEmailException emailException) {
    final var errorMessage = "Error while sending email";
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMessage, emailException));
  }

  @ExceptionHandler(S3GeneralException.class)
  public ResponseEntity<Object> handleS3GeneralException(S3GeneralException s3GeneralException) {
    final var errorMessage = "Error while working with AWS S3";
    return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, s3GeneralException));
  }

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleIllegalErrors(RuntimeException ex, WebRequest request) {
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getCause().getLocalizedMessage(), ex));
  }

  @ExceptionHandler(value = EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), exception));
  }

  @ExceptionHandler(value = EmptyResultDataAccessException.class)
  protected ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException exception) {
    return buildResponseEntity(new ApiError(HttpStatus.NO_CONTENT));
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }

  @ExceptionHandler({OverdueTokenException.class})
  public ResponseEntity<Object> handleOverdueTokenException(OverdueTokenException tokenException) {
    final var errorMessage = "Dear user, verification link has expired, please register again. You are able to register with the same email and receive new verification link";
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMessage, tokenException));
  }

  @ExceptionHandler({ConfirmEmailException.class})
  public ResponseEntity<Object> handleTokenConfirmException(ConfirmEmailException emailException) {
    final var errorMessage = "Email already confirm";
    return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, errorMessage, emailException));
  }
}
