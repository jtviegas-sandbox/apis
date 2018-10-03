package org.aprestos.labs.api.springboot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.aprestos.labs.api.springboot.model.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;




@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SpringBootTests2 {

  @Autowired
   private MockMvc mockMvc;
  
  @Autowired
  private ObjectMapper jsonMapper;
  
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        
        Message message = new Message();
        message.setText("aaa");
        
        MvcResult result = this.mockMvc.perform(post("/api/messages")
            .contentType("application/json")
            .content(jsonMapper.writeValueAsString(message)))
          .andDo(print()).andExpect(status().isCreated()).andReturn();
        
        String location = result.getResponse().getHeaderValue("Location").toString();
        String ident =   location.substring( location.lastIndexOf('/') + 1 );
        
        result = this.mockMvc.perform(get("/api/messages/{msg-id}", ident)
            .accept("application/json"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
        Message created = jsonMapper.readValue(result.getResponse().getContentAsString(), Message.class);
        Assert.assertEquals(created.getText(), message.getText());
        
        
        result = this.mockMvc.perform(get("/api/messages")
            .accept("application/json"))
            .andDo(print()).andExpect(status().isOk()).andReturn();
        
        List<Message> messages = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Message>>() {
        });
        Assert.assertTrue(0 < messages.size());
        
        
    }


}
