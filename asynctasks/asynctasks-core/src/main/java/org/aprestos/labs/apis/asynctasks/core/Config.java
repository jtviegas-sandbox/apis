package org.aprestos.labs.apis.asynctasks.core;

import org.aprestos.labs.apis.asynctasks.common.services.notifier.TaskStateManager;
import org.aprestos.labs.apis.asynctasks.core.solvers.KnapsackSolver;
import org.aprestos.labs.apis.asynctasks.core.solvers.KnapsackSolverImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.WebApplicationContext;


@Import(org.aprestos.labs.apis.asynctasks.common.Boot.class)
@ComponentScan(basePackages = { "org.aprestos.labs.apis.asynctasks.common.services.notifier" })
@EnableAutoConfiguration
@Configuration
public class Config {

	@Value("${app.blocking-coefficient}")
	private double blockingCoefficient;

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

		if (1 < blockingCoefficient || 0 > blockingCoefficient)
			throw new RuntimeException("blockingCoefficient should be in between [0.0,1.0]");

		// one decimal only
		blockingCoefficient = ((int) (blockingCoefficient / 0.1)) * 0.1;

		int corePoolSize = Runtime.getRuntime().availableProcessors();
		int maxPoolSize = (int) (blockingCoefficient == 1.0 ? corePoolSize * 10
				: corePoolSize / (1 - blockingCoefficient));

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setThreadNamePrefix("knapsack-core-pool-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		executor.setDaemon(true);
		return executor;
	}
	
	@Bean(name = "knapsackSolver")
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
	public KnapsackSolver knapsackSolver(@Autowired TaskStateManager taskStateManager) {
	  return new KnapsackSolverImpl(taskStateManager);
	}

}
