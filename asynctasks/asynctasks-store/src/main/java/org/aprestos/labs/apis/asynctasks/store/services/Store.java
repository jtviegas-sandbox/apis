package org.aprestos.labs.apis.asynctasks.store.services;

import java.util.Set;

import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;

public interface Store {

	TaskWrapper getTask(String ident) throws StoreException;

	Set<TaskWrapper> getTasks() throws StoreException;

	void postTask(TaskWrapper task) throws StoreException;

}
