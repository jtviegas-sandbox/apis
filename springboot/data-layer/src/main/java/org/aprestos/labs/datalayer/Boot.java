package org.aprestos.labs.datalayer;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.aprestos.labs.datalayer.repositories")
@EntityScan("org.aprestos.labs.datalayer.model")
@ComponentScan(basePackages = "org.aprestos.labs.datalayer.services")
@Configuration
public class Boot {

}
