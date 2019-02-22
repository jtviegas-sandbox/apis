package org.aprestos.labs.apis.springboot2.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.aprestos.labs.apis.springboot2.model.dto.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(value = "/api/messages", consumes = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = { "messages" }, value = "API root for messages.")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
    @ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Messages {
  private static final Logger logger = LoggerFactory.getLogger(Messages.class);
  private static final AtomicInteger idGenerator = new AtomicInteger(0);

  @RequestMapping(method = RequestMethod.POST)
  @ApiOperation(value = "Used to post a message", response = Message.class)
  @io.swagger.annotations.ApiResponses(value = {
      @ApiResponse(code = 200, message = "successful operation", response = Message.class) })
  public ResponseEntity<Message> postMessage( @RequestBody @Valid Message message ) {
    logger.trace("[postMessage] in");
    try {
      message.setIdent( Integer.toString(idGenerator.incrementAndGet()) );
      MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
      header.add("Content-Type", "application/json");

      return new ResponseEntity<>(message, header, HttpStatus.OK);
    } finally {
      logger.trace("[postMessage] out");
    }
  }


}
