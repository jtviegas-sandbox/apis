package org.aprestos.labs.datalayer.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.aprestos.labs.datalayer.model.Data;
import org.aprestos.labs.datalayer.repositories.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class DataServiceImpl implements DataService {

  @Autowired
  private DataRepository repository;

  public DataServiceImpl() {
  }

  @Override
  public Optional<Data> find(Long ident) {
    return repository.findById(ident);
  }

  @Override
  public Long save(Data data) {
    return repository.save(data).getId();
  }

  @Override
  public void delete(Long ident) {
    repository.deleteById(ident);
  }

  @Override
  public Set<Data> get() {
    return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toSet());
  }

}
