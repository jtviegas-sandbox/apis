package org.aprestos.labs.apis.asynctasks.common.exceptions;

public class TaskStateException extends Exception {

  private static final long serialVersionUID = 1L;

  public TaskStateException() {
  }

  public TaskStateException(String message) {
    super(message);
  }

  public TaskStateException(Throwable cause) {
    super(cause);
  }

  public TaskStateException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
