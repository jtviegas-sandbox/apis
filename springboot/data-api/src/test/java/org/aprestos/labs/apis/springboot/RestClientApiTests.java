package org.aprestos.labs.apis.springboot;

import java.util.List;
import java.util.Optional;

import org.aprestos.labs.apiclient.RestClient;
import org.aprestos.labs.apis.springboot.datamodel.dto.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RestClientApiTests {

  @Autowired
  private RestClient restClient;
  
	@Test
	public void getMessages() {
	  
	  MultiValueMap<String, String> header = restClient.getHeadersBuilder().build();
	  
	  ResponseEntity<List<Message>> response = restClient.getResponse(new ParameterizedTypeReference<List<Message>>() {
    }, header, "http://localhost:3001/api/messages", null, (Object) null);
	  
	  Assert.assertEquals(HttpStatus.OK, response.getStatusCode() );
	  Assert.assertTrue(0 < response.getBody().size());

	}
	
	@Test
  public void postMessages() {
	  
    MultiValueMap<String, String> header = restClient.getHeadersBuilder().build();
    Message message = new Message( "olarilolela");
    
    ResponseEntity<Void> response = restClient.post(message, header, "http://localhost:3001/api/messages");
    Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String location = response.getHeaders().get("Location").get(0);
    Long ident = Long.parseLong(location.substring(location.lastIndexOf('/')+1));
    
    Optional<Message> responseMsg = restClient.get(new ParameterizedTypeReference<Message>(){}, header, "http://localhost:3001/api/messages/{msg-id}", null, ident);
    Assert.assertTrue(responseMsg.isPresent());
    Assert.assertEquals(message.getText(), responseMsg.get().getText());
    
    response = restClient.delete(header, "http://localhost:3001/api/messages/{msg-id}", ident);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

  }

}
