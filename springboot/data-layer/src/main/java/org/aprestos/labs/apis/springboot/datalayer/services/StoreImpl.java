package org.aprestos.labs.apis.springboot.datalayer.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.aprestos.labs.apis.springboot.datalayer.repositories.DataRepository;
import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;
import org.aprestos.labs.apis.springboot.datamodel.transform.Data2Message;
import org.aprestos.labs.apis.springboot.datamodel.transform.Message2Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreImpl implements Store {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(StoreImpl.class);

  @Autowired
  private DataRepository repository;
  private final Data2Message data2message = new Data2Message();
  private final Message2Data message2data = new Message2Data();
  
  public List<Message> getMessages() {
    LOGGER.trace("[getMessages|in]");
    List<Message> messages = new ArrayList<Message>();
    Iterator<Data> iterator = repository.findAll().iterator();
    while(iterator.hasNext()) 
      messages.add( data2message.apply(iterator.next()) );

    LOGGER.trace("[getMessages|out]");
    return messages;
  }

  @Override
  public Long postMessage(Message msg) {
    LOGGER.trace("[postMessage|in]");
    LOGGER.trace("[postMessage|out]");
    Data data = repository.save(message2data.apply(msg));
    return data.getId();
  }

  @Override
  public Message getMessage(Long ident) {
    LOGGER.trace("[getMessage|in]");
    Message msg = null;
    Optional<Data> data = repository.findById(ident);
    if(data.isPresent()) 
      msg = data2message.apply(data.get());
    LOGGER.trace("[getMessage|out]");
    return msg;
  }

  @Override
  public void delMessage(Long ident) {
    LOGGER.trace("[delMessage|in]");
    repository.deleteById(ident);
    LOGGER.trace("[delMessage|out]");
  }

}
