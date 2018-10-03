package org.aprestos.labs.api.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(org.aprestos.labs.apiclient.Bootstrap.class)
@Configuration
public class TestConfig {
  
}
