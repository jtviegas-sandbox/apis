package org.aprestos.labs.apis.springboot2.store.repositories;

import org.aprestos.labs.apis.springboot2.model.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, String> {

}
