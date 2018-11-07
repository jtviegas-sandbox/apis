package org.challenges.maersk.knapsack.core.services;

import java.util.ArrayList;
import java.util.List;

import org.challenges.maersk.common.model.Solution;
import org.challenges.maersk.common.model.TaskStatus;
import org.challenges.maersk.common.model.TaskWrapper;
import org.challenges.maersk.common.services.notifier.TaskStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.ortools.algorithms.KnapsackSolver;

public class SolverTask implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(SolverTask.class);

	private static final long MILLI = 1000;

	private final TaskStateManager taskStateManager;
	private final TaskWrapper task;

	public SolverTask(final TaskStateManager taskStateManager, final TaskWrapper task) {
		LOGGER.trace("[()|in] task: {}", task);
		this.taskStateManager = taskStateManager;
		this.task = task;
		LOGGER.trace("[()|out]");
	}

	@Override
	public void run() {

		LOGGER.trace("[run|in]");
		KnapsackSolver solver = new KnapsackSolver(KnapsackSolver.SolverType.KNAPSACK_DYNAMIC_PROGRAMMING_SOLVER,
				"knapsackDynamic");
		long[] profits = task.getProblem().getValues().stream().mapToLong(l -> l.longValue()).toArray();
		long[][] weigths = new long[1][];
		weigths[0] = task.getProblem().getWeights().stream().mapToLong(l -> l.longValue()).toArray();
		long[] capacity = new long[1];
		capacity[0] = task.getProblem().getCapacity();
		try {

			solver.init(profits, weigths, capacity);

			final long start = System.currentTimeMillis() / MILLI;
			this.task.getTask().getTimestamps().setStarted(start);
			this.task.getTask().setStatus(TaskStatus.started);

			LOGGER.info("[run] task solution started {}", start);
			solver.solve();
			final long end = System.currentTimeMillis() / MILLI;
			LOGGER.info("[run] task solution ended {}", end);
			final int time = (int) (end - start);

			List<Integer> solutions = new ArrayList<Integer>();
			for (int i = 0; i < profits.length; i++)
				if (solver.bestSolutionContains(i))
					solutions.add(i);

			Solution solution = new Solution(solutions, time);
			LOGGER.info("[run] task solution took {}s and is: {}", time, solution);
			this.task.getTask().setStatus(TaskStatus.completed);
			this.task.setSolution(solution);
			this.task.getTask().getTimestamps().setCompleted(end);

			this.taskStateManager.notify(task);
			LOGGER.info("[run] task state was notified");

		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			LOGGER.trace("[run|out]");
		}
	}

}
