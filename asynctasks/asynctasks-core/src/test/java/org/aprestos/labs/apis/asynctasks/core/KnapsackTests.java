package org.aprestos.labs.apis.asynctasks.core;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.aprestos.labs.apis.asynctasks.common.model.Task;
import org.aprestos.labs.apis.asynctasks.core.solvers.KnapsackSolver;
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
public class KnapsackTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;

	@MockBean
	private KnapsackSolver solver;

/*	@MockBean
	private Solver knapsack;*/

	@Test
	public void test_001_shouldAcceptTask() throws Exception {
	  
	  Map<String,Object> problem = new HashMap<String,Object>();
	  problem.put("capacity", 60);
	  problem.put("values", Arrays.asList(23.0,12.0,9.0,3.0));
	  problem.put("weights", Arrays.asList(23,23,44,5));
	  
	  Task task = new Task();
	  task.setProblem(problem);
	  
	  when(solver.submit(task)).thenReturn("1234567");

		this.mockMvc.perform(post("/asynctasks/knapsack").contentType("application/json")
				.content(jsonMapper.writeValueAsString(task))).andExpect(status().isOk());

	}

	@Test
	public void test_002_shouldAcceptOnlyValidTasks() throws Exception {
		String wrongJson = "{\"prop\": 1234}";
		mockMvc.perform(post("/asynctasks/knapsack").contentType("application/json").content(wrongJson))
				.andExpect(status().is(422));
	}
}
