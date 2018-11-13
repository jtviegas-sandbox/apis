package org.aprestos.labs.apis.asynctasks.store.resources;

import java.util.Set;

import javax.validation.Valid;

import org.aprestos.labs.apis.asynctasks.common.exceptions.ApiException;
import org.aprestos.labs.apis.asynctasks.common.model.TaskWrapper;
import org.aprestos.labs.apis.asynctasks.store.services.Store;
import org.aprestos.labs.apis.asynctasks.store.services.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value = "/knapsack/store")
@Api(tags = { "tasks store api" }, value = "API root for tasks store")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
		@ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Tasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);

	private final Store store;

	public Tasks(@Autowired Store store) {
		this.store = store;
	}

	@RequestMapping(value = "/tasks/{task-id}", method = RequestMethod.GET)
	@ApiOperation(value = "Uniquely identifies the task instance", notes = "", response = TaskWrapper.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "successful operation", response = TaskWrapper.class),
			@ApiResponse(code = 404, message = "no match", response = void.class) })
	public ResponseEntity<TaskWrapper> getTask(@ApiParam @PathVariable("task-id") String ident) throws ApiException {
		LOGGER.trace("[getTask] in", ident);
		TaskWrapper task = null;
		try {
			if (null == (task = store.getTask(ident)))
				throw new ApiException(String.format("no task found: %s", ident), HttpStatus.NOT_FOUND);

			return new ResponseEntity<TaskWrapper>(task, HttpStatus.OK);
		} catch (StoreException e) {
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[getTask] out", task);
		}
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.POST)
	@ApiOperation(value = "Used to post a task", notes = "", response = Void.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = Void.class) })
	public ResponseEntity<Void> postTask(@RequestBody @Valid TaskWrapper task) throws ApiException {
		LOGGER.trace("[postTask] in");
		try {
			store.postTask(task);
			LOGGER.trace("[postTask] posted task: {}", task);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[postTask] out");
		}
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	@ApiOperation(value = "Used to retrieve all the tasks", notes = "", response = TaskWrapper.class, responseContainer = "Set")
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = TaskWrapper.class, responseContainer = "Set") })
	public ResponseEntity<Set<TaskWrapper>> getTasks() throws ApiException {
		LOGGER.trace("[getTasks] in");
		try {
			return new ResponseEntity<Set<TaskWrapper>>(store.getTasks(), HttpStatus.OK);
		} catch (StoreException e) {
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[getTasks] out");
		}
	}

}
