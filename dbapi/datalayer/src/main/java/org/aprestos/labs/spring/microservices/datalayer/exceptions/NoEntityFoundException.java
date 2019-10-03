package org.aprestos.labs.spring.microservices.datalayer.exceptions;

public class NoEntityFoundException extends DataLayerException {

  private static final long serialVersionUID = 1L;

  public NoEntityFoundException() {
  }

  public NoEntityFoundException(String message) {
    super(message);
  }

  public NoEntityFoundException(Throwable cause) {
    super(cause);
  }

  public NoEntityFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoEntityFoundException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
