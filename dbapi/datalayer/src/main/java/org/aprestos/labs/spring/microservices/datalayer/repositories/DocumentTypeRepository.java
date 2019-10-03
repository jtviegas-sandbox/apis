package org.aprestos.labs.spring.microservices.datalayer.repositories;

import org.aprestos.labs.spring.microservices.datamodel.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DocumentTypeRepository extends JpaRepository<DocumentType, String> {

}
