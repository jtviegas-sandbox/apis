package org.aprestos.labs.api.bootexpone.services;

import java.util.List;

import org.aprestos.labs.api.bootexpone.model.Message;

public interface Store {
  List<Message> getMessages();
}
