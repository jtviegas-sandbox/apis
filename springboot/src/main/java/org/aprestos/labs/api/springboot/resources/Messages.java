package org.aprestos.labs.api.springboot.resources;

import java.util.List;

import javax.validation.Valid;

import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.api.springboot.services.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/messages")
@Api(tags = { "messages" }, value = "API root for messages.")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
    @ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Messages {
  private static final Logger logger = LoggerFactory.getLogger(Messages.class);
  
  private final Store store;
  
  public Messages(@Autowired Store store) {
    this.store = store;
  }
/*  
  @Autowired
 */

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
  
  @RequestMapping(method = RequestMethod.POST)
  @ApiOperation(value = "Used to post a message", notes = "", response = Void.class)
  @io.swagger.annotations.ApiResponses(value = {
      @ApiResponse(code = 201, message = "successful operation", response = Void.class) })
  public ResponseEntity<Void> postMessage( @RequestBody @Valid Message message ) {
    logger.trace("[postMessage] in");
    try {
      Long ident = store.postMessage(message);
       MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
       header.add("Content-Type", "application/json");
       header.add("Location", String.format("/api/messages/%d", ident.intValue()));
      return new ResponseEntity<Void>(header, HttpStatus.CREATED);
    } finally {
      logger.trace("[postMessage] out");
    }
  }
  
  @RequestMapping(value = "/{msg-id}", method = RequestMethod.GET)
  @ApiOperation(value = "Uniquely identifies the message instance", notes = "", response = Message.class)
  @io.swagger.annotations.ApiResponses(value = {
      @ApiResponse(code = 200, message = "successful operation", response = Message.class),
      @ApiResponse(code = 404, message = "no match", response = void.class) })
  public ResponseEntity<Message> getMessage(@ApiParam @PathVariable("msg-id") Long ident) throws Exception {
    logger.trace("[getMessage] in", ident);
    Message message = null;
    try {
      if (null == (message = store.getMessage(ident)))
        throw new Exception(String.format("no message found for id: %s", ident.toString()));

      return new ResponseEntity<Message>(message, HttpStatus.OK);
    } finally {
      logger.trace("[getMessage] out", message);
    }
  }
  
  @RequestMapping(value = "/{msg-id}", method = RequestMethod.DELETE)
  @ApiOperation(value = "Permanently deletes the message record", notes = "", response = void.class)
  @io.swagger.annotations.ApiResponses(value = {
      @ApiResponse(code = 200, message = "successful operation", response = void.class),
      @ApiResponse(code = 404, message = "no match", response = void.class) })
  public ResponseEntity<Void> deleteMessage(@ApiParam @PathVariable("msg-id") Long ident)
      throws Exception {
    logger.trace("[deleteMessage] in", ident);
    try {
      store.delMessage(ident);
      return new ResponseEntity<Void>(HttpStatus.OK);
    } finally {
      logger.trace("[deleteMessage] out");
    }
  }

}
