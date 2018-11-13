package org.aprestos.labs.apis.asynctasks.core.services;

import java.util.ArrayList;
import java.util.List;

import org.aprestos.labs.apis.asynctasks.common.model.Solution;
import org.aprestos.labs.apis.asynctasks.common.model.TaskStatus;
import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;
import org.aprestos.labs.apis.asynctasks.common.services.notifier.TaskStateManager;
import org.jorlib.alg.knapsack.BinaryKnapsack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		double[] profits = task.getProblem().getValues().stream().mapToDouble(l -> l.doubleValue()).toArray();
		int[] weigths = task.getProblem().getWeights().stream().mapToInt(l -> l.intValue()).toArray();
		int capacity = task.getProblem().getCapacity();
		
		try {
			final long start = System.currentTimeMillis() / MILLI;
			this.task.getTask().getTimestamps().setStarted(start);
			this.task.getTask().setStatus(TaskStatus.started);
			
			BinaryKnapsack knapsack=new BinaryKnapsack();
			LOGGER.info("[run] task solution started {}", start);
			knapsack.solveKnapsackProblem(profits.length, capacity, profits, weigths);
			final long end = System.currentTimeMillis() / MILLI;
			LOGGER.info("[run] task solution ended {}", end);
			final int time = (int) (end - start);

			List<Double> solutions = new ArrayList<Double>();
			int i = 0;
			for ( boolean isSolution: knapsack.getKnapsackItems() ) {
			  if(isSolution)
			    solutions.add(profits[i++]);
			}

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
