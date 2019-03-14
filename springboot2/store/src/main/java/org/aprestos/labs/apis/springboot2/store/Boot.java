package org.aprestos.labs.apis.springboot2.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.aprestos.labs.apis.springboot2.store.repositories")
@EntityScan("org.aprestos.labs.apis.springboot2.model.entities")
@ComponentScan(basePackages = "org.aprestos.labs.apis.springboot2.store.services")
@Configuration
public class Boot {

}
