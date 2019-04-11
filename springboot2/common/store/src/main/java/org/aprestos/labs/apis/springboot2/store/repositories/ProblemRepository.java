package org.aprestos.labs.apis.springboot2.store.repositories;

import org.aprestos.labs.apis.springboot2.model.entities.Problem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProblemRepository extends JpaRepository<Problem, Long> {

}
