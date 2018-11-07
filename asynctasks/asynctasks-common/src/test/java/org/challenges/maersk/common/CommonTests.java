package org.challenges.maersk.common;

import java.io.IOException;

import org.challenges.maersk.common.model.TaskWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Boot.class })
public class CommonTests {

	@Autowired
	private ObjectMapper jsonMapper;

	@Test
	public void contextLoads() throws JsonParseException, JsonMappingException, IOException {
		Assert.assertNotNull(jsonMapper);
		String obj = "{\"problem\": {\"capacity\": 60, \"weights\": [10, 20, 33], \"values\": [10, 3, 30]}, "
				+ "\"task\":{\"task\": \"nbd43jhb\", \"status\": \"submitted\", \"timestamps\": {\"submitted\": 1505225308, \"started\": 1505225320, \"completed\": null}}}";
		TaskWrapper task = jsonMapper.readValue(obj, TaskWrapper.class);
	}

}
