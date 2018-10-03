package org.aprestos.labs.api.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.datalayer.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreImpl implements Store {
  
  
  @Autowired
  private DataService dataService;
  
  public List<Message> getMessages() {
    List<Message> r = new ArrayList<Message>();
    final int n = 6;

    for (int i = 0; i < n; i++)
      r.add(new Message(UUID.randomUUID().toString(), RandomStringUtils.random(12, true, false)));

    return r;
  }

}
