package org.aprestos.labs.datalayer;

import org.aprestos.labs.datalayer.services.DataService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
public class DataLayerTests {

  @Autowired
  private DataService dataService;
  
  @Test
  @DatabaseSetup("data_test_get.xml")
  public void test_get() {
    Assert.assertTrue( 2 == dataService.get().size() ); 
  }
  
  @Test
  @DatabaseSetup("data_test_del.xml")
  @ExpectedDatabase(value="expected_test_del.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
  public void test_del() {
    dataService.delete(new Long(4));
  }


}
