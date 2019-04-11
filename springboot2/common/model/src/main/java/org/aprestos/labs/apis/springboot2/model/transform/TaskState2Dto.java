package org.aprestos.labs.apis.springboot2.model.transform;

import org.aprestos.labs.apis.springboot2.model.dto.TaskState;

import java.util.function.Function;

public class TaskState2Dto implements Function<org.aprestos.labs.apis.springboot2.model.entities.TaskState, TaskState> {


    @Override
    public TaskState apply(org.aprestos.labs.apis.springboot2.model.entities.TaskState state) {
        return new TaskState(state.getIdent(), state.getTimestamp(), state.getStatus());
    }
}
