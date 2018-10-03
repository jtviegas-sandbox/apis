package org.aprestos.labs.api.springboot.services;

import java.util.List;

import org.aprestos.labs.api.springboot.model.Message;

public interface Store {
  List<Message> getMessages();
}
