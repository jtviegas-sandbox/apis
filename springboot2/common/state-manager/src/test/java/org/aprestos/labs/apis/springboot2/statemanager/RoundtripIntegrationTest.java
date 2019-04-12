package org.aprestos.labs.apis.springboot2.statemanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aprestos.labs.apiclient.HeadersBuilder;
import org.aprestos.labs.apiclient.RestClient;
import org.aprestos.labs.apis.springboot2.model.dto.Item;
import org.aprestos.labs.apis.springboot2.model.dto.Problem;
import org.aprestos.labs.apis.springboot2.model.dto.Task;
import org.aprestos.labs.apis.springboot2.model.dto.TaskStatus;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoundtripIntegrationTest {

	@Autowired
	private StateManager<String, Task, TaskStatus> manager;

	@Autowired
	private ObjectMapper jsonMapper;

	@Value("${org.aprestos.labs.apis.springboot2.statemanager.uri.collective}")
	private String stateUri;


	@Test
	public void test_dumbOne() throws Exception {

		MultiValueMap<String, String> header = new LinkedMultiValueMap();
		header.add("Content-Type", "application/json");
		header.add("Accept", "application/json");

		Problem problem = new Problem(15,
				new Item[]{new Item(4, 12), new Item(2, 1),
						new Item(2, 2), new Item(1, 1), new Item(10, 4)});

		Task task = new Task(problem);

		manager.notify(task);
		Assert.assertTrue(true);

	}

}
