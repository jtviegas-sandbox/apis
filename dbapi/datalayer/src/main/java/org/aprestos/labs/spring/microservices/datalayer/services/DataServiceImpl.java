package org.aprestos.labs.spring.microservices.datalayer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aprestos.labs.spring.microservices.datalayer.exceptions.NoEntityFoundException;
import org.aprestos.labs.spring.microservices.datalayer.repositories.DataOwnerRepository;
import org.aprestos.labs.spring.microservices.datalayer.repositories.DocumentTypeRepository;
import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.aprestos.labs.spring.microservices.datamodel.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

  @Autowired
  private DataOwnerRepository dataOwnerRepository;
  @Autowired
  private DocumentTypeRepository docTypeRepository;


  @Autowired
  private ObjectMapper jsonMapper;

  @Override
  public List<DocumentType> getDocTypes() {
    log.trace("[getDocTypes] in");
    List<DocumentType> result = docTypeRepository.findAll();
    log.trace("[getDocTypes] out", result);
    return result;
  }

  @Override
  public DataOwner save(DataOwner o) {
    log.trace("[save] in ({})", o);
    DataOwner result = dataOwnerRepository.save(o);
    log.trace("[save] out => {}", result);
    return result;
  }

  @Override
  public void deleteDataOwner(Long id) {
    log.trace("[deleteDataOwner] in ({})", id);
    dataOwnerRepository.deleteById(id);
    log.trace("[deleteDataOwner] out");
  }

  @Override
  public DocumentType save(DocumentType o) {
    log.trace("[save] in ({})", o);
    DocumentType result = docTypeRepository.save(o);
    log.trace("[save] out => {}", result);
    return result;
  }

  @Override
  public List<DataOwner> getDataOwners() {
    log.trace("[getDataOwners] in");
    List<DataOwner> result = dataOwnerRepository.findAll();
    log.trace("[getDataOwners] out => {}", result);
    return result;
  }
  @Override
  public DataOwner getDataOwner(Long id) throws NoEntityFoundException {
    log.trace("[getDataOwner] in ({})", id);

    DataOwner result = null;
    Optional<DataOwner> optRes = dataOwnerRepository.findById(id);
    if( ! optRes.isPresent() )
      throw new NoEntityFoundException(String.format( "dataOwnerId: %d", id));
    else
      result = optRes.get();

    log.trace("[getDataOwner] out => {}", result);
    return result;
  }

}
