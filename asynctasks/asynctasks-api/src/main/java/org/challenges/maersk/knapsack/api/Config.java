package org.challenges.maersk.knapsack.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@ComponentScan(basePackages = { "org.challenges.maersk.common.services" })
@Import(org.challenges.maersk.common.Boot.class)
@EnableAutoConfiguration
@Configuration
public class Config {

	@Bean
	public ObjectMapper jsonMapper() {
		ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule())
				.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

		return mapper;
	}

}
