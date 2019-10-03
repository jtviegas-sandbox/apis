package org.aprestos.labs.spring.microservices.datalayer.exceptions;

public abstract class DataLayerException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataLayerException() {
  }

  public DataLayerException(String message) {
    super(message);
  }

  public DataLayerException(Throwable cause) {
    super(cause);
  }

  public DataLayerException(String message, Throwable cause) {
    super(message, cause);
  }

  public DataLayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
