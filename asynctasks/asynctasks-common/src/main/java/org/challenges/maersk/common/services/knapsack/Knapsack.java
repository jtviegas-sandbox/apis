package org.challenges.maersk.common.services.knapsack;

import org.challenges.maersk.common.model.TaskWrapper;

public interface Knapsack {

	void submitTask(TaskWrapper task) throws KnapsackException;

}