package org.aprestos.labs.apis.asynctasks.core.solvers;

import org.aprestos.labs.apis.asynctasks.common.exceptions.TaskStateException;
import org.aprestos.labs.apis.asynctasks.common.model.Task;

public interface KnapsackSolver {

	void solve();
	String submit(Task task) throws TaskStateException;

}