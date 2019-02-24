package org.aprestos.labs.apis.springboot2.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import org.aprestos.labs.apis.springboot2.exceptions.ApiException;
import org.aprestos.labs.apis.springboot2.model.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/api/messages", consumes = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = { "messages" }, value = "API root for messages.")
@ApiResponses(value = { @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid status value", response = void.class),
    @io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Messages {
  private static final Logger logger = LoggerFactory.getLogger(Messages.class);
  private static final AtomicInteger idGenerator = new AtomicInteger(0);

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Message processValidationError(MethodArgumentNotValidException ex) {
    StringBuilder sb = new StringBuilder();
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    for (FieldError fieldError: fieldErrors)
      sb.append(String.format("%s: %s", fieldError.getField(), messageSource.getMessage(fieldError, Locale.ENGLISH)));

    Message result =  new Message();
    result.setText(sb.toString());
    return result;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ApiOperation(value = "Used to post a message", response = Message.class)
  @io.swagger.annotations.ApiResponses(value = {
      @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation", response = Message.class) })
  public ResponseEntity<Message> postMessage( @RequestBody @Valid Message message ) throws ApiException {
    logger.trace("[postMessage] in");
    try {
      if( message.getText().contains("error") )
        throw new ApiException("ohhh there is an error");

      message.setIdent( Integer.toString(idGenerator.incrementAndGet()) );
      MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
      header.add("Content-Type", "application/json");

      return new ResponseEntity<>(message, header, HttpStatus.OK);
    } finally {
      logger.trace("[postMessage] out");
    }
  }


}
