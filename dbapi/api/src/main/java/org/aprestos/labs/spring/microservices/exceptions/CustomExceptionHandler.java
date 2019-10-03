package org.aprestos.labs.spring.microservices.exceptions;

import static java.lang.String.format;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aprestos.labs.spring.microservices.datalayer.exceptions.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String MSG_FORMAT = "id: %s | path: %s | msg: %s";

  private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

  @ExceptionHandler
  @ResponseBody
  ResponseEntity<ExceptionResponse> handleControllerException(HttpServletRequest request, Throwable ex) {

    HttpStatus status = getStatus(request, ex);

    String logId = logError(ex);
    return new ResponseEntity<ExceptionResponse>(
        ExceptionResponse.createLogReferringExceptionResponse(status.value(), logId), status);
  }

  private String logError(Throwable ex) {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
    String path = builder.buildAndExpand().getPath();
    String logId = UUID.randomUUID().toString();
    logger.error(format(MSG_FORMAT, logId, path, ex.getMessage()), ex);
    return logId;
  }

  private HttpStatus getStatus(HttpServletRequest request, Throwable ex) {
    HttpStatus code = null;
    if (ex instanceof ApiException && null != (code = ((ApiException) ex).getStatusCode())) {
      // note that ApiException constructor sets the code
      return code;
    }
    else if( ex instanceof NoEntityFoundException ){
      return HttpStatus.NOT_FOUND;
    }

    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null)
      return HttpStatus.INTERNAL_SERVER_ERROR;

    return HttpStatus.valueOf(statusCode);
  }

  // this handles invalid fields in json objects and such
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    String logId = logError(ex);
    return new ResponseEntity<>(
        ExceptionResponse.createLogReferringExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), logId),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }

  // this handles validation exception, i.e. @NotNull checks
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    // return super.handleMethodArgumentNotValid(ex, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    String logId = logError(ex);
    return new ResponseEntity<>(
        ExceptionResponse.createLogReferringExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), logId),
        HttpStatus.UNPROCESSABLE_ENTITY);
  }
}
