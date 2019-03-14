package org.aprestos.labs.apis.springboot2.store.services;


import org.aprestos.labs.apis.springboot2.model.dto.MessageDto;

import java.util.List;
import java.util.Optional;

public interface Store {
  List<MessageDto> getMessages();
  String postMessage(MessageDto msg);
  Optional<MessageDto> getMessage(String ident);
  void delMessage(String ident);
}
