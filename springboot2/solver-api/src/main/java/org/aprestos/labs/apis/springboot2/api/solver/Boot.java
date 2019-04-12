package org.aprestos.labs.apis.springboot2.api.solver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableConfigurationProperties
@Import({org.aprestos.labs.apis.springboot2.solver.k.Config.class})
public class Boot {

	public static void main(String[] args) {
		SpringApplication.run(Boot.class, args);
	}

}
