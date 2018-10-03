package org.aprestos.labs.datalayer.services;

import java.util.Optional;
import java.util.Set;

import org.aprestos.labs.datalayer.model.Data;

public interface DataService {
  
  Optional<Data> find(Long ident);
  Long save(Data data);
  void delete(Long ident);
  Set<Data> get();
  
}
