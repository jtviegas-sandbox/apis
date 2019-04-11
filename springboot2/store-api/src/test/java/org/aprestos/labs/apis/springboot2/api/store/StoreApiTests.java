package org.aprestos.labs.apis.springboot2.api.store;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.aprestos.labs.apis.springboot2.model.dto.*;
import org.aprestos.labs.apis.springboot2.testtools.UtilsModel;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AutoConfigureMockMvc
public class StoreApiTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper jsonMapper;


	@Test
	public void test_001_all() throws Exception {

		MvcResult response = this.mockMvc.perform(get("/store").accept("application/json"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		List<Task> tasks = jsonMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Task>>(){});
		Assert.assertEquals(0, tasks.size());

		Task expected = UtilsModel.createTask();
		response = this.mockMvc.perform(post("/store").accept("application/json")
				.contentType("application/json")
				.content(jsonMapper.writeValueAsString(expected)))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		Task saved = jsonMapper.readValue(response.getResponse().getContentAsString(), Task.class);
		Assert.assertNotNull(saved.getId());
		expected.setId(saved.getId());
		Assert.assertNotNull(saved.getProblem().getId());
		expected.getProblem().setId(saved.getProblem().getId());
		Assert.assertEquals(expected.getStatuses().size(), saved.getStatuses().size());
		// propagate problem items ids
		for( Item is: saved.getProblem().getItems() ){
			for( Item ie: expected.getProblem().getItems() ){
				if( ie.getValue().equals(is.getValue()) && ie.getWeight().equals(is.getWeight()) ){
					ie.setId(is.getId());
					break;
				}
			}
		}
		Assert.assertEquals(expected, saved);
		expected.getStatuses().add(new TaskState(System.currentTimeMillis(), TaskStatus.submitted));

		response = this.mockMvc.perform(post("/store").accept("application/json")
				.contentType("application/json")
				.content(jsonMapper.writeValueAsString(expected)))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		response = this.mockMvc.perform(get("/store").accept("application/json"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		tasks = jsonMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Task>>(){});
		Assert.assertEquals(1, tasks.size());

		response = this.mockMvc.perform(get("/store/{task-id}",saved.getId()).accept("application/json"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		saved = jsonMapper.readValue(response.getResponse().getContentAsString(), Task.class);


		Assert.assertEquals(1 , saved.getStatuses().size() );
		expected.getStatuses().get(0).setId(saved.getStatuses().get(0).getId());
		Assert.assertEquals(expected, saved);

		expected.setSolution(new Solution(
				new Item[]{
						new Item( RandomUtils.nextInt(), RandomUtils.nextInt())
						, new Item( RandomUtils.nextInt(), RandomUtils.nextInt())
				}, RandomUtils.nextLong(), RandomUtils.nextInt()
		));

		this.mockMvc.perform(post("/store").accept("application/json")
				.contentType("application/json")
				.content(jsonMapper.writeValueAsString(expected)))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		response = this.mockMvc.perform(get("/store/{task-id}",saved.getId()).accept("application/json"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		saved = jsonMapper.readValue(response.getResponse().getContentAsString(), Task.class);

		Assert.assertNotNull(saved.getSolution().getId());
		expected.getSolution().setId(saved.getSolution().getId());
		for(Item is: saved.getSolution().getItems()){
			for(Item ie: expected.getSolution().getItems()){
				if( ie.getValue().equals(is.getValue()) && ie.getWeight().equals(is.getWeight()) ){
					ie.setId(is.getId());
					break;
				}
			}
		}
		Assert.assertEquals(expected, saved);


		this.mockMvc.perform(delete("/store/{task-id}",saved.getId()).accept("application/json"))
				.andDo(print()).andExpect(status().isOk());

		response = this.mockMvc.perform(get("/store").accept("application/json"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		tasks = jsonMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<List<Task>>(){});
		Assert.assertEquals(0, tasks.size());



	}

	@Test
	public void test_002_shouldAcceptOnlyValidTasks() throws Exception {
		String wrongJson = "{\"prop\": 1234}";
		mockMvc.perform(post("/store").contentType("application/json").content(wrongJson))
				.andExpect(status().is(422));
	}

	@Test
	public void test_003_should404OnDeleteMissingId() throws Exception {
		this.mockMvc.perform(delete("/store/{task-id}", RandomStringUtils.randomAscii(16)).accept("application/json"))
				.andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void test_004_should404OnGetMissingId() throws Exception {
		this.mockMvc.perform(get("/store/{task-id}",RandomStringUtils.randomAscii(16)).accept("application/json"))
				.andDo(print()).andExpect(status().isNotFound());
	}
}
