package org.aprestos.labs.apis.springboot.datalayer.services;

import java.util.List;

import org.aprestos.labs.apis.springboot.datamodel.dto.Message;

public interface Store {
  List<Message> getMessages();
  Long postMessage(Message msg);
  Message getMessage(Long ident);
  void delMessage(Long ident);
}
