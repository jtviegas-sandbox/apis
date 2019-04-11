package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.entities.TaskState;

import java.util.function.Function;

public class TaskState2entity implements Function<org.aprestos.labs.apis.springboot2.model.dto.TaskState, TaskState> {


    @Override
    public TaskState apply(org.aprestos.labs.apis.springboot2.model.dto.TaskState state) {
        TaskState result = new TaskState();

        result.setIdent(state.getId());
        result.setStatus(state.getStatus());
        result.setTimestamp(state.getTimestamp());

        return result;
    }
}
