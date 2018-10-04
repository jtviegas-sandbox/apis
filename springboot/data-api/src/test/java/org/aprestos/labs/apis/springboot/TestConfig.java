package org.aprestos.labs.apis.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(org.aprestos.labs.apiclient.Bootstrap.class)
@Configuration
public class TestConfig {
  
}
