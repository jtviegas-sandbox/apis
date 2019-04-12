package org.aprestos.labs.apis.springboot2.api.solver;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.aprestos.labs.apiclient.RestClient;
import org.aprestos.labs.apis.springboot2.model.dto.Item;
import org.aprestos.labs.apis.springboot2.model.dto.Problem;
import org.aprestos.labs.apis.springboot2.model.dto.Solution;
import org.aprestos.labs.apis.springboot2.model.dto.Task;
import org.aprestos.labs.apis.springboot2.testtools.UtilsModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SolverApiIntegrationTest {

	@Autowired
	private com.fasterxml.jackson.databind.ObjectMapper jsonMapper;

	private final String SOLVER_ENDPOINT = "http://localhost:7705/solver";
	private final String STORE_ENDPOINT = "http://localhost:7700/store/task/{task-id}";

	@Before
	public void init(){
		Unirest.setObjectMapper(new ObjectMapper() {

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jsonMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jsonMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Test
	public void test_simple_problem() throws Exception {

		Problem expected = new Problem(12, new Item[]{new Item(2,6), new Item(8,6), new Item(2,6), new Item(2,6)});
		HttpResponse<String> response = Unirest.post(SOLVER_ENDPOINT)
				.header("accept", "text/plain")
				.header("Content-Type", "application/json")
				.body(expected).asString();

		Assert.assertEquals(HttpStatus.OK.value(), response.getStatus());
		String id = response.getBody();
		Assert.assertNotNull(id);
		Thread.sleep(10000);

		HttpResponse<Task> response2 = Unirest.get(STORE_ENDPOINT)
				.header("accept", "application/json")
				.header("Content-Type", "application/json")
				.routeParam("task-id", id).asObject(Task.class);
		Assert.assertNotNull(response2.getBody().getSolution());
		Solution solution = response2.getBody().getSolution();
		Assert.assertEquals(2, solution.getItems().length);
		Assert.assertEquals(10, solution.getValue().intValue());


	}


}
