package org.aprestos.labs.apis.asynctasks.core;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.aprestos.labs.apis.asynctasks.common.model.Problem;
import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;
import org.aprestos.labs.apis.asynctasks.common.services.knapsack.Knapsack;
import org.aprestos.labs.apis.asynctasks.common.services.notifier.TaskStateManager;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KnapsackCoreTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private TaskStateManager taskStateManager;

	@MockBean
	private Knapsack knapsack;

	@Test
	public void test_001_shouldAcceptTask() throws Exception {

		TaskWrapper expected = TaskWrapper.fromProblem(new Problem(RandomUtils.nextInt(1, 32),
				Arrays.asList(RandomUtils.nextInt(1, 32), RandomUtils.nextInt(1, 32), RandomUtils.nextInt(1, 32)),
				Arrays.asList(RandomUtils.nextDouble(1, 32), RandomUtils.nextDouble(1, 32), RandomUtils.nextDouble(1, 32))));

		this.mockMvc.perform(post("/knapsack/core/task").contentType("application/json")
				.content(jsonMapper.writeValueAsString(expected))).andExpect(status().isOk());

	}

	@Test
	public void test_002_shouldAcceptOnlyValidTasks() throws Exception {
		String wrongJson = "{\"prop\": 1234}";
		mockMvc.perform(post("/knapsack/core/task").contentType("application/json").content(wrongJson))
				.andExpect(status().is(422));
	}
}
