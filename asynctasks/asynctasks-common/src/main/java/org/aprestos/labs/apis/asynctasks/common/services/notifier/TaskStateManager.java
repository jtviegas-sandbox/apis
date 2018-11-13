package org.aprestos.labs.apis.asynctasks.common.services.notifier;

import java.util.Optional;
import java.util.Set;

import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;

public interface TaskStateManager {

	void notify(TaskWrapper state) throws TaskStateManagerException;

	Optional<TaskWrapper> getState(String ident) throws StateNotFoundException, TaskStateManagerException;

	Set<TaskWrapper> getStates() throws TaskStateManagerException;

}
