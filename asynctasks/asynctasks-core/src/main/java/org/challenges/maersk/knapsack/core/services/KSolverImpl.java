package org.challenges.maersk.knapsack.core.services;

import javax.annotation.PreDestroy;

import org.challenges.maersk.common.model.TaskWrapper;
import org.challenges.maersk.common.services.notifier.TaskStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
class KSolverImpl implements KSolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KSolverImpl.class);

	private final TaskStateManager taskStateManager;
	private final ThreadPoolTaskExecutor executor;

	public KSolverImpl(@Autowired ThreadPoolTaskExecutor executor, @Autowired TaskStateManager taskStateManager) {
		LOGGER.trace("[()|in]");
		this.taskStateManager = taskStateManager;
		this.executor = executor;
		LOGGER.trace("[()|out]");
	}

	@Override
	public void solve(TaskWrapper task) {
		LOGGER.trace("[solve|in] task: {}", task);
		this.executor.submit(new SolverTask(taskStateManager, task));
		LOGGER.trace("[solve|out]");
	}

	@PreDestroy
	public void shutdown() {
		LOGGER.trace("[shutdown|in]");
		LOGGER.trace("[shutdown|out]");
	}

}
