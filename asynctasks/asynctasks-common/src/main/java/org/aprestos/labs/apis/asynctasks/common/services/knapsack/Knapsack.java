package org.aprestos.labs.apis.asynctasks.common.services.knapsack;

import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;

public interface Knapsack {

	void submitTask(TaskWrapper task) throws KnapsackException;

}