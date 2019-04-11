package org.aprestos.labs.apis.springboot2.store.repositories;

import org.aprestos.labs.apis.springboot2.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, String> {

}
