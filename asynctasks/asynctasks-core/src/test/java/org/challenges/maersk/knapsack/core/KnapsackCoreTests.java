package org.challenges.maersk.knapsack.core;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.apache.commons.lang3.RandomUtils;
import org.challenges.maersk.common.model.Problem;
import org.challenges.maersk.common.model.TaskWrapper;
import org.challenges.maersk.common.services.knapsack.Knapsack;
import org.challenges.maersk.common.services.notifier.TaskStateManager;
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

		TaskWrapper expected = TaskWrapper.fromProblem(new Problem(RandomUtils.nextLong(1, 32),
				Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32)),
				Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32))));

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
