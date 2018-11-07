package org.challenges.maersk.knapsack.api.resources;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.challenges.maersk.common.exceptions.ApiException;
import org.challenges.maersk.common.model.ProblemWrapper;
import org.challenges.maersk.common.model.SolutionWrapper;
import org.challenges.maersk.common.model.Task;
import org.challenges.maersk.common.model.TaskWrapper;
import org.challenges.maersk.common.model.TasksCollection;
import org.challenges.maersk.common.services.knapsack.Knapsack;
import org.challenges.maersk.common.services.knapsack.KnapsackException;
import org.challenges.maersk.common.services.notifier.StateNotFoundException;
import org.challenges.maersk.common.services.notifier.TaskStateManager;
import org.challenges.maersk.common.services.notifier.TaskStateManagerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/knapsack")
@Api(tags = { "tasks api" }, value = "API root for knapsack tasks")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
		@ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Tasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);
	private static final long MILLI = 1000;

	private final TaskStateManager notifier;
	private final Knapsack knapsack;

	public Tasks(final @Autowired TaskStateManager notifier, final @Autowired Knapsack knapsack) {
		this.notifier = notifier;
		this.knapsack = knapsack;
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	@ApiOperation(value = "Used to post a knapsack task problem", notes = "", response = Task.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = Task.class) })
	public ResponseEntity<Task> postTask(@RequestBody @Valid ProblemWrapper problemWrapper) throws ApiException {
		LOGGER.trace("[postTask|in] problemWrapper: {}", problemWrapper);
		try {
			TaskWrapper task = TaskWrapper.fromProblem(problemWrapper.getProblem());

			task.getTask().getTimestamps().setSubmitted(System.currentTimeMillis() / MILLI);
			notifier.notify(task);
			this.knapsack.submitTask(task);

			MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
			header.add("Content-Type", "application/json");
			header.add("id", task.getTask().getTask());
			return new ResponseEntity<Task>(task.getTask(), header, HttpStatus.CREATED);

		} catch (KnapsackException | TaskStateManagerException e) {
			LOGGER.error("something wrong when tryint to post a task", e);
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[postTask|out]");
		}
	}

	@RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Used to get a previously submitted knapsack task", response = Task.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = Task.class),
			@ApiResponse(code = 404, message = "no match", response = void.class) })
	public ResponseEntity<Task> getTask(@ApiParam @PathVariable("id") String ident) throws ApiException {
		LOGGER.trace("[getTask|in] ident: {}", ident);
		Task task = null;
		try {
			Optional<TaskWrapper> wrapper = notifier.getState(ident);
			if (wrapper.isPresent())
				task = wrapper.get().getTask();
			else
				throw new ApiException(String.format("no task found with id %s", ident), HttpStatus.NOT_FOUND);

			return new ResponseEntity<Task>(task, HttpStatus.OK);
		} catch (StateNotFoundException snfe) {
			throw new ApiException("state not found", snfe, HttpStatus.NOT_FOUND);
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[getTask|out] {}", task);
		}
	}

	@RequestMapping(value = "/solutions/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Used to get a solution for a previously submitted knapsack task", response = SolutionWrapper.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = SolutionWrapper.class),
			@ApiResponse(code = 404, message = "no match", response = void.class) })
	public ResponseEntity<SolutionWrapper> getSolution(@ApiParam @PathVariable("id") String ident) throws ApiException {
		LOGGER.trace("[getSolution|in] ident: {}", ident);
		SolutionWrapper result = null;
		try {
			Optional<TaskWrapper> wrapper = notifier.getState(ident);
			if (!(wrapper.isPresent() && (null != wrapper.get().getSolution()))) {
				throw new ApiException(String.format("no solution found for task with id %s", ident),
						HttpStatus.NOT_FOUND);
			}

			result = new SolutionWrapper(ident, wrapper.get().getProblem(), wrapper.get().getSolution());

			return new ResponseEntity<SolutionWrapper>(result, HttpStatus.OK);
		} catch (StateNotFoundException snfe) {
			throw new ApiException("state not found", snfe, HttpStatus.NOT_FOUND);
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[getSolution|out] {}", result);
		}
	}

	@RequestMapping(value = "/admin/tasks", method = RequestMethod.GET)
	@ApiOperation(value = "Used to get a list of submitted knapsack tasks", notes = "", response = TasksCollection.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = TasksCollection.class) })
	public ResponseEntity<TasksCollection> getTasks() throws ApiException {
		LOGGER.trace("[getTasks|in]");
		TasksCollection tasks = null;
		try {
			Set<TaskWrapper> wrappers = notifier.getStates();
			tasks = TasksCollection.fromTasks(wrappers);
			return new ResponseEntity<TasksCollection>(tasks, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[getTasks|out] {}", tasks);
		}
	}

}
