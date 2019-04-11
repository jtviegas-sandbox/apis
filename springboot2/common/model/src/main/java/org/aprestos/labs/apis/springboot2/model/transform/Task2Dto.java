package org.aprestos.labs.apis.springboot2.model.transform;


import org.aprestos.labs.apis.springboot2.model.dto.Task;
import org.aprestos.labs.apis.springboot2.model.dto.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Task2Dto implements Function<org.aprestos.labs.apis.springboot2.model.entities.Task, Task> {

    @Override
    public Task apply(org.aprestos.labs.apis.springboot2.model.entities.Task task) {

        Problem2Dto problemMapper = new Problem2Dto();
        Task result = new Task(problemMapper.apply(task.getProblem()));
        if(null != task.getSolution()){
            Solution2Dto mapper = new Solution2Dto();
            result.setSolution(mapper.apply(task.getSolution()));
        }
        result.setId(task.getIdent());

        if( null != task.getStatuses() && !task.getStatuses().isEmpty() ){
            TaskState2Dto mapper = new TaskState2Dto();
            for(org.aprestos.labs.apis.springboot2.model.entities.TaskState taskState: task.getStatuses())
                result.getStatuses().add(mapper.apply(taskState));
        }

        return result;
    }
}
