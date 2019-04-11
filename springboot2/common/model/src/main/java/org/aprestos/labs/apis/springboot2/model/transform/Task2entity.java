package org.aprestos.labs.apis.springboot2.model.transform;


import org.aprestos.labs.apis.springboot2.model.entities.Problem;
import org.aprestos.labs.apis.springboot2.model.entities.Solution;
import org.aprestos.labs.apis.springboot2.model.entities.Task;

import java.util.function.Function;

public class Task2entity implements Function<org.aprestos.labs.apis.springboot2.model.dto.Task, Task> {

    @Override
    public Task apply(org.aprestos.labs.apis.springboot2.model.dto.Task task) {

        Task result = new Task();
        Problem2entity problemMapper = new Problem2entity();
        Problem problem = problemMapper.apply(task.getProblem());
        result.setProblem(problem);

        if( null != task.getSolution() ) {
            Solution solution = new Solution2entity().apply(task.getSolution());
            result.setSolution(solution);
        }

        result.setIdent(task.getId());

        if( null != task.getStatuses() && !task.getStatuses().isEmpty() ){
            TaskState2entity mapper = new TaskState2entity();
            for(org.aprestos.labs.apis.springboot2.model.dto.TaskState state: task.getStatuses())
                result.getStatuses().add(mapper.apply(state));
        }

        return result;
    }
}
