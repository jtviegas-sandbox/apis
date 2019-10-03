package org.aprestos.labs.spring.microservices.resources;

import lombok.extern.slf4j.Slf4j;
import org.aprestos.labs.spring.microservices.datalayer.exceptions.NoEntityFoundException;
import org.aprestos.labs.spring.microservices.datalayer.services.DataService;
import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.aprestos.labs.spring.microservices.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class Controller {

  private final DataService service;

  public Controller(@Autowired DataService dataService) {
    this.service = dataService;
  }

  @RequestMapping(value = "/dataOwner/{dataOwnerId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteDataOwner(@NotNull @PathVariable("dataOwnerId") Long dataOwnerId ) {
    log.trace("[deleteDataOwner] in ({})", dataOwnerId);
    try {
      service.deleteDataOwner(dataOwnerId);
      return new ResponseEntity<Void>(HttpStatus.OK);
    } finally {
      log.trace("[deleteDataOwner] out");
    }
  }

  @RequestMapping(value = "/dataOwner/{dataOwnerId}", method = RequestMethod.GET)
  public ResponseEntity<DataOwner> getDataOwner(@NotNull @PathVariable("dataOwnerId") Long id) throws NoEntityFoundException {
    log.trace("[getDataOwner] in ({})", id);
    DataOwner result = null;
    try {
      result = service.getDataOwner(id);
      return new ResponseEntity<DataOwner>(result, HttpStatus.OK);
    } finally {
      log.trace("[getDataOwner] out => {}", result);
    }
  }


  @RequestMapping(value = "/dataOwner", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
  public ResponseEntity<DataOwner> postDataOwner(@NotNull @RequestBody @Valid DataOwner dataOwner)
      throws ApiException {
    log.trace("[postDataOwner] in ({})", dataOwner);
    DataOwner result = null;
    try {
      result = service.save(dataOwner);
      return new ResponseEntity<DataOwner>(result, HttpStatus.CREATED);
    } finally {
      log.trace("[postDataOwner] out => {}", result);
    }

  }

  @RequestMapping(value = "/dataOwner", method = RequestMethod.GET)
  public ResponseEntity<List<DataOwner>> getDataOwners()  {
    log.trace("[getDataOwners] in");
    List<DataOwner> result = null;
    try {
      result = service.getDataOwners();
      return new ResponseEntity<List<DataOwner>>(result, HttpStatus.OK);
    } finally {
      log.trace("[getDataOwners] out => {}", result);
    }
  }

}
