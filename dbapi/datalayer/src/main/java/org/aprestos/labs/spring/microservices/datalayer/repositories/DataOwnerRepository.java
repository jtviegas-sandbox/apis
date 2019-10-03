package org.aprestos.labs.spring.microservices.datalayer.repositories;


import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataOwnerRepository extends JpaRepository<DataOwner, Long> {

}
