package org.aprestos.labs.apis.springboot2.api.services;

import org.aprestos.labs.apis.springboot2.model.dto.Problem;
import org.aprestos.labs.apis.springboot2.model.dto.Task;

import java.util.List;
import java.util.Optional;

public interface Client {
    String post(Problem problem) throws Exception;

    Optional<Task> get(String taskId) throws Exception;

    List<Task> get() throws Exception;
}
