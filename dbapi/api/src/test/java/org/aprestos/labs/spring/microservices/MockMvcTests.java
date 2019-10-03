package org.aprestos.labs.spring.microservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.aprestos.labs.spring.microservices.datalayer.services.DataService;
import org.aprestos.labs.spring.microservices.datamodel.DataOwner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(classes = Bootstrap.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MockMvcTests {

  @Autowired
  private DataService service;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper jsonMapper;


  @Test
  public void test_00() throws Exception {

    int initialSize = service.getDataOwners().size();
    MvcResult result = this.mockMvc.perform(get("/api/dataOwner").accept("application/json")
            .contentType("application/json"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    List<DataOwner> outMsg = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<DataOwner> >(){});
    Assert.assertEquals(initialSize, outMsg.size());

    DataOwner o = new DataOwner();
    o.setName(RandomStringUtils.random(8, true,false));
    service.save(o);

    int size = service.getDataOwners().size();
    Assert.assertEquals(initialSize +1 , size);
    result = this.mockMvc.perform(get("/api/dataOwner").accept("application/json")
            .contentType("application/json"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    outMsg = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<DataOwner> >(){});
    Assert.assertEquals(size, outMsg.size());

    o = new DataOwner();
    o.setName(RandomStringUtils.random(8, true,false));

    this.mockMvc.perform(post("/api/dataOwner").accept("application/json")
            .contentType("application/json")
            .content(jsonMapper.writeValueAsString(o)))
            .andDo(print()).andExpect(status().isCreated());

    size = service.getDataOwners().size();
    Assert.assertEquals(initialSize +2 , size);
    result = this.mockMvc.perform(get("/api/dataOwner").accept("application/json")
            .contentType("application/json"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
    outMsg = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<DataOwner> >(){});
    Assert.assertEquals(size, outMsg.size());


  }

}
