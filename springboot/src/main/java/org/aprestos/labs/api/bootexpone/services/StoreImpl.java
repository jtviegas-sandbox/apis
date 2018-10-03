package org.aprestos.labs.api.bootexpone.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.aprestos.labs.api.bootexpone.model.Message;
import org.springframework.stereotype.Service;

@Service
public class StoreImpl implements Store {

  public List<Message> getMessages() {
    List<Message> r = new ArrayList<Message>();
    final int n = 6;

    for (int i = 0; i < n; i++)
      r.add(new Message(UUID.randomUUID().toString(), RandomStringUtils.random(12, true, false)));

    return r;
  }

}
