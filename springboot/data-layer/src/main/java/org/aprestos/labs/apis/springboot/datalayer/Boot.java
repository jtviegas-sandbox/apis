package org.aprestos.labs.apis.springboot.datalayer;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "org.aprestos.labs.apis.springboot.datalayer.repositories")
@EntityScan("org.aprestos.labs.apis.springboot.datamodel.schemas")
@ComponentScan(basePackages = "org.aprestos.labs.apis.springboot.datalayer.services")
@Configuration
public class Boot {

}
