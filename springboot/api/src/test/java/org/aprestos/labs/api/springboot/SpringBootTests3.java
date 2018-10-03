package org.aprestos.labs.api.springboot;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.api.springboot.resources.Messages;
import org.aprestos.labs.api.springboot.services.Store;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;




//@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
@WebMvcTest(Messages.class)
public class SpringBootTests3 {

  @Autowired
   private MockMvc mockMvc;
  
  @MockBean
  private Store store;
  
  @Autowired
  private ObjectMapper jsonMapper;
  
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        
        Long ident = new Long(100);
        Message message = new Message();
        message.setText("aaa");
        
        List<Message> list =  Arrays.asList(new Message(ident.toString(), "aaa"), new Message("101", "bbb"));
        
        when(store.postMessage(message)).thenReturn( ident );
        when(store.getMessage(ident)).thenReturn( new Message(ident.toString(), "aaa") );
        when(store.getMessages()).thenReturn(list);
        
        
        MvcResult result = this.mockMvc.perform(post("/api/messages")
            .contentType("application/json")
            .content(jsonMapper.writeValueAsString(message)))
          .andDo(print()).andExpect(status().isCreated()).andReturn();
        
        result = this.mockMvc.perform(get("/api/messages/{msg-id}", "100")
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
