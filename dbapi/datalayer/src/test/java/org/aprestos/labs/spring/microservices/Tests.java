package org.aprestos.labs.spring.microservices;

import org.apache.commons.lang3.RandomStringUtils;
import org.aprestos.labs.spring.microservices.datalayer.exceptions.NoEntityFoundException;
import org.aprestos.labs.spring.microservices.datalayer.services.DataService;
import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = {DataLayerConf.class})
@RunWith(SpringRunner.class)
public class Tests {

  @Autowired
  private DataService service;

  @Test
  public void test_00() throws NoEntityFoundException {

    int initialSize = service.getDataOwners().size();
    DataOwner o = new DataOwner();
    o.setName(RandomStringUtils.random(23, true,false));
    service.save(o);

    Assert.assertEquals(initialSize+1, service.getDataOwners().size());
  }

}
