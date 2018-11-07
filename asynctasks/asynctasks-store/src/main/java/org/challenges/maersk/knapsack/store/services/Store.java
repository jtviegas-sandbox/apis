package org.challenges.maersk.knapsack.store.services;

import java.util.Set;

import org.challenges.maersk.common.model.TaskWrapper;

public interface Store {

	TaskWrapper getTask(String ident) throws StoreException;

	Set<TaskWrapper> getTasks() throws StoreException;

	void postTask(TaskWrapper task) throws StoreException;

}
