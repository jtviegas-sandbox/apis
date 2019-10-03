package org.aprestos.labs.spring.microservices.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends Exception {

  private static final long serialVersionUID = 1L;

  private static final int STACK_DEPTH = 5;

  private HttpStatus statusCode;

  public ApiException() {
  }

  public ApiException(String message) {
    super(message);
  }

  public ApiException(String message, HttpStatus statusCode) {
    super(message);
    this.statusCode = statusCode;
  }

  public ApiException(Throwable cause) {
    super(cause);
    if (cause instanceof IllegalArgumentException) {
      // this is the case when i.e. null identifiers are given for arguments to repository methods
      this.statusCode = HttpStatus.UNPROCESSABLE_ENTITY;
    } else {
      // dive into stack trace and examine oracle errors
      for (int i = 0; i < STACK_DEPTH && cause != null && this.statusCode == null; i++) {
        // System.err.println(i + ": " + cause.getMessage()); // testing only
        if (cause instanceof java.sql.SQLIntegrityConstraintViolationException) {
            this.statusCode = HttpStatus.CONFLICT;
        }
        cause = cause.getCause();
      }
      // exception that was not handled in data layer
      if (this.statusCode == null)
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    }
  }

  public ApiException(Throwable cause, HttpStatus statusCode) {
    super(cause);
    this.statusCode = statusCode;
  }

  public ApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApiException(String message, Throwable cause, HttpStatus statusCode) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
      HttpStatus statusCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.statusCode = statusCode;
  }

  public HttpStatus getStatusCode() {
    return statusCode;
  }

}
