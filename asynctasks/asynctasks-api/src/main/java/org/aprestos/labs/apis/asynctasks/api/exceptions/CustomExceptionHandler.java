package org.aprestos.labs.apis.asynctasks.api.exceptions;

import static java.lang.String.format;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.aprestos.labs.apis.asynctasks.common.exceptions.ApiException;
import org.aprestos.labs.apis.asynctasks.common.exceptions.ExceptionResponse;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@ExceptionHandler
	@ResponseBody
	ResponseEntity<ExceptionResponse> handleControllerException(final HttpServletRequest request, final Throwable ex) {

		final HttpStatus status = getStatus(request, ex);
		return new ResponseEntity<ExceptionResponse>(
				ExceptionResponse.createLogReferringExceptionResponse(status.value(), logError(ex)), status);
	}

	private String logError(final Throwable ex) {
		final UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
		final String path = builder.buildAndExpand().getPath();
		final String logId = UUID.randomUUID().toString();
		LOGGER.error(format(MSG_FORMAT, logId, path, ex.getMessage()), ex);
		return logId;
	}

	private HttpStatus getStatus(final HttpServletRequest request, final Throwable ex) {
		HttpStatus code = null;
		if (ex instanceof ApiException && null != (code = ((ApiException) ex).getStatusCode())) {
			// note that ApiException constructor sets the code
			return code;
		}

		final Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null)
			return HttpStatus.INTERNAL_SERVER_ERROR;

		return HttpStatus.valueOf(statusCode);
	}

	// this handles invalid fields in json objects and such
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		return new ResponseEntity<>(ExceptionResponse.createLogReferringExceptionResponse(
				HttpStatus.UNPROCESSABLE_ENTITY.value(), logError(ex)), HttpStatus.UNPROCESSABLE_ENTITY);
	}

	// this handles validation exception, i.e. @NotNull checks
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		return new ResponseEntity<>(ExceptionResponse.createLogReferringExceptionResponse(
				HttpStatus.UNPROCESSABLE_ENTITY.value(), logError(ex)), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
