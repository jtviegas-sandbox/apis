package org.aprestos.labs.apis.springboot2.store.repositories;

import org.aprestos.labs.apis.springboot2.model.entities.Solution;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SolutionRepository extends JpaRepository<Solution, Long> {

}
