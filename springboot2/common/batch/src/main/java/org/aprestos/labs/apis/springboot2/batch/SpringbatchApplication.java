package org.aprestos.labs.apis.springboot2.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class SpringbatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbatchApplication.class, args);
    }
}
