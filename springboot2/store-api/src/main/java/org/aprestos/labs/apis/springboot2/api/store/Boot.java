package org.aprestos.labs.apis.springboot2.api.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


@EnableConfigurationProperties
@Import({org.aprestos.labs.apis.springboot2.store.Boot.class})
@SpringBootApplication(exclude={JacksonAutoConfiguration.class} )
public class Boot {

	public static void main(String[] args) {
		SpringApplication.run(Boot.class, args);
	}

	@Bean
	public ObjectMapper jsonMapper() {
		ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module())
				.registerModule(new JavaTimeModule());

		return mapper;
	}

}
