package org.aprestos.labs.apis.springboot.datalayer.repositories;

import org.aprestos.labs.apis.springboot.datamodel.schemas.Data;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<Data, Long> {

}
