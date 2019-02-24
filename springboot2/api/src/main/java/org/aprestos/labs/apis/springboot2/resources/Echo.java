package org.aprestos.labs.apis.springboot2.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.aprestos.labs.apis.springboot2.exceptions.ApiException;
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

@RestController
@RequestMapping(value = "/api/echo", consumes = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = { "echo" }, value = "API root for echo function.")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
    @ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Echo {
  private static final Logger logger = LoggerFactory.getLogger(Echo.class);

  @RequestMapping(method = RequestMethod.POST)
  @ApiOperation(value = "Used to post a message", response = Message.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "successful operation", response = Message.class) })
  public ResponseEntity<Message> postEcho( @RequestBody @Valid Message message ) throws ApiException {
    logger.trace("[postEcho] in");
    try {
      if( message.getText().contains("error") )
        throw new ApiException("ohhh there is an error");

      MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
      header.add("Content-Type", "application/json");
      return new ResponseEntity<>(message, header, HttpStatus.OK);
    } finally {
      logger.trace("[postEcho] out");
    }
  }


}
