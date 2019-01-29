package org.aprestos.labs.apis.asynctasks.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
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
		String obj = "{ \"capacity\": 60, \"weights\": [10, 20, 33], \"values\": [10, 3, 30]}";
		Map<String,Object> problem = jsonMapper.readValue(obj, new TypeReference<Map<String,Object>>() {
    });
		Assert.assertEquals(60, problem.get("capacity"));
		Assert.assertEquals(Arrays.asList(10, 20, 33), (List<Integer>)problem.get("weights"));
	}

}
