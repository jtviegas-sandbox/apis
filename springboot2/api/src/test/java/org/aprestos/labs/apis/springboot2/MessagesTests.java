package org.aprestos.labs.apis.springboot2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aprestos.labs.apis.springboot2.exceptions.ExceptionResponse;
import org.aprestos.labs.apis.springboot2.model.dto.Message;
import org.junit.Assert;
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




@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class MessagesTests {

  @Autowired
   private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper jsonMapper;
  
    @Test
    public void messageStateRoundTrip() throws Exception {

        Message message = new Message();
        message.setText("aaa");
        
        
        MvcResult result = this.mockMvc.perform(post("/api/messages").accept("application/json")
                .contentType("application/json")
            .content(jsonMapper.writeValueAsString(message)))
          .andDo(print()).andExpect(status().isOk()).andReturn();

        Message outMsg = jsonMapper.readValue(result.getResponse().getContentAsString(), Message.class);
        Assert.assertTrue(0 < Integer.parseInt(outMsg.getIdent()) );
        Assert.assertTrue(System.currentTimeMillis() > outMsg.getTimestamp() );

    }

    @Test
    public void messageValidationControllerExceptionHandler() throws Exception {

        Message message = new Message();
        message.setText(null);
        MvcResult result =  this.mockMvc.perform(post("/api/messages").accept("application/json")
                .contentType("application/json")
                .content(jsonMapper.writeValueAsString(message)))
                .andDo(print()).andExpect(status().is4xxClientError()).andReturn();

        Message response = jsonMapper.readValue(result.getResponse().getContentAsString(), Message.class);
        Assert.assertEquals("text: must not be null", response.getText());
    }

    @Test
    public void messageValidationServerException() throws Exception {

        Message message = new Message();
        message.setText("error");
        MvcResult result =  this.mockMvc.perform(post("/api/messages").accept("application/json")
                .contentType("application/json")
                .content(jsonMapper.writeValueAsString(message)))
                .andDo(print()).andExpect(status().isInternalServerError()).andReturn();
        ExceptionResponse response = jsonMapper.readValue(result.getResponse().getContentAsString(), ExceptionResponse.class);
        Assert.assertTrue(response.getMsg().startsWith("something was not correct"));
    }

}
