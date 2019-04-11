package org.aprestos.labs.apis.springboot2.solver.k;

import lombok.extern.slf4j.Slf4j;
import org.aprestos.labs.apis.springboot2.statemanager.StateManager;
import org.aprestos.labs.apis.springboot2.model.dto.Problem;
import org.aprestos.labs.apis.springboot2.model.dto.Task;
import org.aprestos.labs.apis.springboot2.model.dto.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
@Slf4j
class SolverImpl implements Solver {

	@Autowired
	private StateManager<String, Task, TaskStatus> stateManager;
	@Autowired
	private ThreadPoolTaskExecutor executor;

	@Override
	public String solve(Problem problem) {
		log.trace("[solve|in] problem: {}", problem);
		String result = null;
		Task task = new Task(problem);
		this.executor.submit(new SolverTask(stateManager, task));
		result = task.getId();
		log.trace("[solve|out] => {}", result);
		return result;
	}

	@PreDestroy
	public void shutdown() {
		log.trace("[shutdown|in]");
		log.trace("[shutdown|out]");
	}

}
