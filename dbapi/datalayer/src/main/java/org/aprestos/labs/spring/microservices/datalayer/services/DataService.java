package org.aprestos.labs.spring.microservices.datalayer.services;

import org.aprestos.labs.spring.microservices.datalayer.exceptions.NoEntityFoundException;
import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.aprestos.labs.spring.microservices.datamodel.DocumentType;

import java.util.List;

public interface DataService {
    List<DocumentType> getDocTypes();

    DataOwner save(DataOwner o);

    void deleteDataOwner(Long id);

    DocumentType save(DocumentType o);

    List<DataOwner> getDataOwners();

    DataOwner getDataOwner(Long id) throws NoEntityFoundException;
}
