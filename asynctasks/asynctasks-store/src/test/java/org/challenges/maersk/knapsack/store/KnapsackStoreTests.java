package org.challenges.maersk.knapsack.store;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.challenges.maersk.common.model.Problem;
import org.challenges.maersk.common.model.TaskWrapper;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KnapsackStoreTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@Test
	public void test_001_shouldStoreTaskGetItAndCheckIt() throws Exception {

		TaskWrapper expected = TaskWrapper.fromProblem(new Problem(RandomUtils.nextLong(1, 32),
				Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32)),
				Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32))));
		MvcResult result = this.mockMvc.perform(post("/knapsack/store/tasks").contentType("application/json")
				.content(jsonMapper.writeValueAsString(expected))).andExpect(status().isOk()).andReturn();
		result = this.mockMvc.perform(
				get("/knapsack/store/tasks/{task-id}", expected.getTask().getTask()).contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		TaskWrapper actual = jsonMapper.readValue(result.getResponse().getContentAsString(), TaskWrapper.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_002_shouldStoreOnlyValidTasks() throws Exception {
		String wrongJson = "{\"prop\": 1234}";
		mockMvc.perform(post("/knapsack/store/tasks").contentType("application/json").content(wrongJson))
				.andExpect(status().is(422));
	}

	@Test
	public void test_0025_shouldStoreOnlyValidTasks2() throws Exception {
		String wrongJson = "{\n" + "  \"problem\": {\n" + "    \"capacity\": 50,\n" + "    \"values\": [\n"
				+ "      12\n" + "    ],\n" + "    \"weigths\": [\n" + "      5, 4\n" + "    ]\n" + "  }\n" + "}";
		mockMvc.perform(post("/knapsack/store/tasks").contentType("application/json").content(wrongJson))
				.andExpect(status().is(422));
	}

	@Test
	public void test_003_shouldSayNotFoundToUnknownTasks() throws Exception {
		mockMvc.perform(get("/knapsack/store/tasks/{task-id}", 123456789).contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void test_004_shouldGetTasksOnTheWhole() throws Exception {

		MvcResult result = mockMvc.perform(get("/knapsack/store/tasks").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		Set<TaskWrapper> tasks = jsonMapper.readValue(result.getResponse().getContentAsString(),
				new TypeReference<Set<TaskWrapper>>() {
				});
		int count = tasks.size();

		mockMvc.perform(post("/knapsack/store/tasks").contentType("application/json")
				.content(jsonMapper.writeValueAsString(TaskWrapper.fromProblem(new Problem(RandomUtils.nextLong(1, 32),
						Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32),
								RandomUtils.nextLong(1, 32)),
						Arrays.asList(RandomUtils.nextLong(1, 32), RandomUtils.nextLong(1, 32),
								RandomUtils.nextLong(1, 32)))))))
				.andExpect(status().isOk());

		result = mockMvc.perform(get("/knapsack/store/tasks").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		tasks = jsonMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Set<TaskWrapper>>() {
		});
		Assert.assertEquals(count + 1, tasks.size());
	}

}
