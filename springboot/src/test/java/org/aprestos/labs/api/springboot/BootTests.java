package org.aprestos.labs.api.springboot;

import java.util.List;

import org.aprestos.labs.api.springboot.model.Message;
import org.aprestos.labs.apiclient.RestClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Import(org.aprestos.labs.apiclient.Bootstrap.class)
@RunWith(SpringRunner.class)
public class BootTests {

  @Autowired
  private RestClient restClient;
  
  
	@Test
	public void getMessages() {
	  MultiValueMap<String, String> h = restClient.getHeadersBuilder().build();
	  
	  ResponseEntity<List<Message>> response = restClient.getResponse(new ParameterizedTypeReference<List<Message>>() {
    }, h, "http://localhost:3001/api/messages", null, (Object) null);
	  
	  Assert.assertEquals(HttpStatus.OK, response.getStatusCode() );
	  Assert.assertTrue(0 < response.getBody().size());

	}

}
