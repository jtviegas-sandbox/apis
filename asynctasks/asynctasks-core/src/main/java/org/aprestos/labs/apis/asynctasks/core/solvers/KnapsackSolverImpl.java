package org.aprestos.labs.apis.asynctasks.core.solvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.aprestos.labs.apis.asynctasks.common.exceptions.TaskStateException;
import org.aprestos.labs.apis.asynctasks.common.model.Status;
import org.aprestos.labs.apis.asynctasks.common.model.StatusType;
import org.aprestos.labs.apis.asynctasks.common.model.Task;
import org.aprestos.labs.apis.asynctasks.common.services.notifier.TaskStateManager;
import org.jorlib.alg.knapsack.BinaryKnapsack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KnapsackSolverImpl implements KnapsackSolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(KnapsackSolverImpl.class);

	private final TaskStateManager taskStateManager;

	public KnapsackSolverImpl(TaskStateManager taskStateManager) {
		LOGGER.trace("[()|in]");
		this.taskStateManager = taskStateManager;
		LOGGER.trace("[()|out]");
	}
	
	private Task task;

	@Override
  public String submit(Task task) throws TaskStateException {
	  LOGGER.trace("[submit|in] task: {}", task);
	  String id = null;
    try {
      this.task = task;
      this.task.getStatusMap().put(System.currentTimeMillis() / 1000, StatusType.submitted);
      @SuppressWarnings("unchecked")
      Future<String> future = (Future<String>) taskStateManager.notify(task);
      id = future.get();
      this.task.setId(id);
      return id;
    } catch (Exception e) {
      throw new TaskStateException(e);
    } finally {
      LOGGER.trace("[submit|out] id:{}", id);
    }
    
    
  }
	
	@Override
	public void solve() {
		LOGGER.trace("[solve|in]");
    try {
      if(null == task)
        throw new RuntimeException("task was not set");
      
      @SuppressWarnings("unchecked")
      double[] values = ((List<Double>)task.getProblem().get("values")).stream().mapToDouble(l -> l.doubleValue()).toArray();
      @SuppressWarnings("unchecked")
      int[] weigths = ((List<Integer>)task.getProblem().get("values")).stream().mapToInt(l -> l.intValue()).toArray();
      int capacity = (int) task.getProblem().get("capacity");
      long start = System.currentTimeMillis() / 1000;
      taskStateManager.notify(Status.create(task.getId(), StatusType.started, start));
      
      BinaryKnapsack knapsack=new BinaryKnapsack();
      LOGGER.info("[solve] task solution started {}", start);
      knapsack.solveKnapsackProblem(values.length, capacity, values, weigths);
      final long end = System.currentTimeMillis() / 1000;
      LOGGER.info("[solve] task solution ended {}", end);
      final int time = (int) (end - start);

      List<Double> solutions = new ArrayList<Double>();
      int i = 0;
      for ( boolean isSolution: knapsack.getKnapsackItems() ) {
        if(isSolution)
          solutions.add(values[i++]);
      }

      Map<String,Object> solution = new HashMap<String,Object>();
      solution.put("values", solutions);
      LOGGER.info("[solve] task solution took {}s and is: {}", time, solution);
      
      this.taskStateManager.notify(Status.create(task.getId(), StatusType.completed, end, solution));
      LOGGER.info("[solve] task state was notified");

    } catch (Exception e) {
      LOGGER.error("[solve]", e);
    } finally {
      LOGGER.trace("[solve|out]");
    }
	}

}
