package org.aprestos.labs.datalayer.repositories;

import org.aprestos.labs.datalayer.model.Data;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<Data, Long> {

}
