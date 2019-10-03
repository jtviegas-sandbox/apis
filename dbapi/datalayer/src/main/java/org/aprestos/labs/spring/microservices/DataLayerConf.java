package org.aprestos.labs.spring.microservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EntityScan("org.aprestos.labs.spring.microservices.datamodel")
@ComponentScan("org.aprestos.labs.spring.microservices.datalayer.services")
@EnableJpaRepositories(basePackages = "org.aprestos.labs.spring.microservices.datalayer.repositories")
@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
public class DataLayerConf {

  @Bean
  public ObjectMapper jsonMapper() {
    ObjectMapper mapper = new ObjectMapper().registerModule(new ParameterNamesModule()).registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());

    return mapper;
  }

}
