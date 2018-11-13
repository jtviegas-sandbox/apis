package org.aprestos.labs.apis.asynctasks.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.aprestos.labs.apis.asynctasks.common.model.Problem;
import org.aprestos.labs.apis.asynctasks.common.model.ProblemWrapper;
import org.aprestos.labs.apis.asynctasks.common.model.Solution;
import org.aprestos.labs.apis.asynctasks.common.model.SolutionWrapper;
import org.aprestos.labs.apis.asynctasks.common.model.Task;
import org.aprestos.labs.apis.asynctasks.common.model.TaskStatus;
import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;
import org.aprestos.labs.apis.asynctasks.common.model.TasksCollection;
import org.aprestos.labs.apis.asynctasks.common.services.knapsack.Knapsack;
import org.aprestos.labs.apis.asynctasks.common.services.notifier.TaskStateManager;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KnapsackApiTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private TaskStateManager taskStateManager;

	@MockBean
	private Knapsack knapsack;

	@Test
	public void test_001_postShouldBeOkAndReturnId() throws Exception {

		ProblemWrapper problemWrapper = new ProblemWrapper(
				new Problem(60, Arrays.asList(10, 20, 33), Arrays.asList(10.0, 3.0, 30.0)));
		MvcResult result = mockMvc
				.perform(post("/knapsack/tasks").contentType("application/json")
						.content(jsonMapper.writeValueAsString(problemWrapper)))
				.andExpect(status().isCreated()).andReturn();
		Assert.assertNotNull(result.getResponse().getHeader("id"));

	}

	@Test
	public void test_002_postShouldRequireValidInput() throws Exception {
		mockMvc.perform(post("/knapsack/tasks").contentType("application/json")
				.content("{\"a\": 23, \"prop\":\"Greenspan is on the podcast\"}")).andExpect(status().is(422));
	}

	@Test
	public void test_003_shouldGetRightNumOfTasksThere() throws Exception {

		MvcResult result = mockMvc.perform(get("/knapsack/admin/tasks").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();

		TasksCollection tasks = jsonMapper.readValue(result.getResponse().getContentAsString(), TasksCollection.class);
		int numOfTasks = tasks.getStarted().size() + tasks.getSubmitted().size() + tasks.getCompleted().size();

		ProblemWrapper problemWrapper = new ProblemWrapper(
				new Problem(60, Arrays.asList(10, 20, 33), Arrays.asList(10.0, 3.0, 30.0)));

		result = mockMvc
				.perform(post("/knapsack/tasks").contentType("application/json")
						.content(jsonMapper.writeValueAsString(problemWrapper)))
				.andExpect(status().isCreated()).andReturn();
		String id = result.getResponse().getHeader("id");

		TaskWrapper task = TaskWrapper.fromProblem(problemWrapper.getProblem());
		Set<TaskWrapper> taskWrappers = new HashSet<TaskWrapper>();
		taskWrappers.add(task);
		when(taskStateManager.getStates()).thenReturn(taskWrappers);

		result = mockMvc.perform(get("/knapsack/admin/tasks").contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		tasks = jsonMapper.readValue(result.getResponse().getContentAsString(), TasksCollection.class);
		Assert.assertEquals(numOfTasks + 1,
				tasks.getStarted().size() + tasks.getSubmitted().size() + tasks.getCompleted().size());

	}

	@Test
	public void test_004_shouldGetTaskFromId() throws Exception {

		ProblemWrapper problemWrapper = new ProblemWrapper(
        new Problem(60, Arrays.asList(10, 20, 33), Arrays.asList(10.0, 3.0, 30.0)));
		MvcResult result = this.mockMvc
				.perform(post("/knapsack/tasks").contentType("application/json")
						.content(jsonMapper.writeValueAsString(problemWrapper)))
				.andExpect(status().isCreated()).andReturn();
		final String taskId = result.getResponse().getHeader("id");
		TaskWrapper expected = TaskWrapper.fromProblem(problemWrapper.getProblem());
		when(taskStateManager.getState(taskId)).thenReturn(Optional.of(expected));

		result = this.mockMvc.perform(get("/knapsack/tasks/{task-id}", taskId).contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		Task task = jsonMapper.readValue(result.getResponse().getContentAsString(), Task.class);
		Assert.assertNotNull(task.getTimestamps().getSubmitted());
		Assert.assertEquals(expected.getTask(), task);

	}

	@Test
	public void test_005_shouldGetNotFoundTaskForNotThereClearly() throws Exception {
		this.mockMvc.perform(get("/knapsack/tasks/{task-id}", 123456789).contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	@Test // (timeout = 60000)
	public void test_006_shouldGetSolutionForSubmittedTask() throws Exception {

		ProblemWrapper problemWrapper = new ProblemWrapper(
        new Problem(60, Arrays.asList(10, 20, 33), Arrays.asList(10.0, 3.0, 30.0)));
		MvcResult result = this.mockMvc
				.perform(post("/knapsack/tasks").contentType("application/json")
						.content(jsonMapper.writeValueAsString(problemWrapper)))
				.andExpect(status().isCreated()).andReturn();
		final String taskId = result.getResponse().getHeader("id");
		TaskWrapper expectedTaskWrapper = TaskWrapper.fromProblem(problemWrapper.getProblem());
		expectedTaskWrapper.getTask().setStatus(TaskStatus.completed);

		Solution solution = new Solution(Arrays.asList(2.0, 3.0, 4.0), 125);
		expectedTaskWrapper.setSolution(solution);

		SolutionWrapper expectedSolutionWrapper = new SolutionWrapper(taskId, expectedTaskWrapper.getProblem(),
				solution);

		when(taskStateManager.getState(taskId)).thenReturn(Optional.of(expectedTaskWrapper));

		TaskStatus status = TaskStatus.submitted;
		while (!status.equals(TaskStatus.completed)) {
			result = this.mockMvc.perform(get("/knapsack/tasks/{task-id}", taskId).contentType("application/json"))
					.andExpect(status().isOk()).andReturn();
			Task task = jsonMapper.readValue(result.getResponse().getContentAsString(), Task.class);
			status = task.getStatus();
		}

		result = mockMvc.perform(get("/knapsack/solutions/{task-id}", taskId).contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		SolutionWrapper actualSolutionWrapper = jsonMapper.readValue(result.getResponse().getContentAsString(),
				SolutionWrapper.class);
		Assert.assertEquals(expectedSolutionWrapper, actualSolutionWrapper);

	}

	@Test
	public void test_007_shouldGetNotFoundSolutionForNotThereClearly() throws Exception {
		this.mockMvc.perform(get("/knapsack/solutions/{task-id}", 123456789).contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	/*
	 * @Test public void test_008_shouldSeeShutdown() throws Exception {
	 * this.mockMvc.perform(post("/knapsack/admin/shutdown").contentType(
	 * "application/json")) .andExpect(status().isOk()); }
	 */

}
