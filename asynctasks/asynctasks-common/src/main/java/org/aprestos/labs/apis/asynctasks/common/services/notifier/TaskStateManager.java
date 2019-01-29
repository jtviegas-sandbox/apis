package org.aprestos.labs.apis.asynctasks.common.services.notifier;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import org.aprestos.labs.apis.asynctasks.common.model.Status;
import org.aprestos.labs.apis.asynctasks.common.model.Task;

public interface TaskStateManager {

  Future<?> notify(Task task);
	Future<?> notify(Status status);
	Optional<Status> getStatus(String id) throws StateNotFoundException, TaskStateManagerException;
	Set<Status> getStatuses() throws TaskStateManagerException;

}
