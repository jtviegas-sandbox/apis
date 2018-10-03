package org.aprestos.labs.api.springboot.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.datalayer.model.Data;
import org.aprestos.labs.datalayer.services.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class StoreImpl implements Store {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(StoreImpl.class);

  
  @Autowired
  private DataService dataService;
  
  public List<Message> getMessages() {
    LOGGER.trace("[getMessages|in]");
    List<Message> messages = new ArrayList<Message>();
    
    for( Data data: dataService.get() ) {
      messages.add(new Message(UUID.randomUUID().toString(), data.getDescription()));
    }
    LOGGER.trace("[getMessages|out]");
    return messages;
  }

  @Override
  public Long postMessage(Message msg) {
    LOGGER.trace("[postMessage|in]");
    LOGGER.trace("[postMessage|out]");
    return dataService.save(Data.of(msg.getText(), LocalDate.now()));
  }

  @Override
  public Message getMessage(Long ident) {
    LOGGER.trace("[getMessage|in]");
    Message msg = null;
    Optional<Data> data = dataService.find(ident);
    if(data.isPresent()) 
      msg = new Message(data.get().getId().toString(), data.get().getDescription());
    LOGGER.trace("[getMessage|out]");
    return msg;
  }

  @Override
  public void delMessage(Long ident) {
    LOGGER.trace("[delMessage|in]");
    dataService.delete(ident);
    LOGGER.trace("[delMessage|out]");
  }

}
