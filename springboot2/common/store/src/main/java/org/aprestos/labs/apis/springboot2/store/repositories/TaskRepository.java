package org.aprestos.labs.apis.springboot2.store.repositories;

import org.aprestos.labs.apis.springboot2.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface TaskRepository extends JpaRepository<Task, String> {

}
