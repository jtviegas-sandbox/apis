package org.aprestos.labs.api.springboot.resources;

import java.util.List;

import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.api.springboot.services.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/messages")
@Api(tags = { "messages" }, value = "API root for messages.")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
    @ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Messages {
  private static final Logger logger = LoggerFactory.getLogger(Messages.class);

  @Autowired
  private Store store;

  @RequestMapping(method = RequestMethod.GET)
  @ApiOperation(value = "Used to retrieve messages", notes = "", response = Message.class, responseContainer = "List")
  @io.swagger.annotations.ApiResponses(value = {
      @ApiResponse(code = 200, message = "successful operation", response = Message.class, responseContainer = "List") })
  public ResponseEntity<List<Message>> getMessages() {
    logger.trace("[getMessages] in");
    try {
      return new ResponseEntity<List<Message>>(store.getMessages(), HttpStatus.OK);
    } finally {
      logger.trace("[getMessages] out");
    }
  }

}
