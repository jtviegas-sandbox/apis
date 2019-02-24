package org.aprestos.labs.apis.springboot2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aprestos.labs.apis.springboot2.exceptions.ExceptionResponse;
import org.aprestos.labs.apis.springboot2.model.dto.Message;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EchoTests {

  @Autowired
   private MockMvc mockMvc;
  
/*  @MockBean
  private Store store;*/
  
  @Autowired
  private ObjectMapper jsonMapper;
  
    @Test
    public void messageStateRoundTrip() throws Exception {

        Message message = new Message();
        message.setText("aaa");
        
        
        MvcResult result = this.mockMvc.perform(post("/api/echo").accept("application/json")
                .contentType("application/json")
            .content(jsonMapper.writeValueAsString(message)))
          .andDo(print()).andExpect(status().isOk()).andReturn();

        Message response = jsonMapper.readValue(result.getResponse().getContentAsString(), Message.class);
        Assert.assertEquals(message.getText(), response.getText());

    }

    @Test
    public void validation() throws Exception {

        Message message = new Message();
        message.setText(null);
        MvcResult result =  this.mockMvc.perform(post("/api/echo").accept("application/json")
                .contentType("application/json")
                .content(jsonMapper.writeValueAsString(message)))
                .andDo(print()).andExpect(status().isUnprocessableEntity()).andReturn();

        ExceptionResponse response = jsonMapper.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
        Assert.assertEquals("text: must not be null", response.getDetails());
    }

    @Test
    public void serverException() throws Exception {

        Message message = new Message();
        message.setText("error");
        this.mockMvc.perform(post("/api/echo").accept("application/json")
                .contentType("application/json")
                .content(jsonMapper.writeValueAsString(message)))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();
    }


}
