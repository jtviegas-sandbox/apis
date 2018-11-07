package org.challenges.maersk.knapsack.core.resources;

import javax.validation.Valid;

import org.challenges.maersk.common.exceptions.ApiException;
import org.challenges.maersk.common.model.TaskWrapper;
import org.challenges.maersk.knapsack.core.services.KSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/knapsack/core")
@Api(tags = { "tasks knapsack core api" }, value = "API root for knapsack tasks core")
@ApiResponses(value = { @ApiResponse(code = 400, message = "Invalid status value", response = void.class),
		@ApiResponse(code = 500, message = "Internal server error", response = void.class) })
public class Tasks {
	private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);

	private final KSolver solver;

	public Tasks(final @Autowired KSolver solver) {
		this.solver = solver;
	}

	@RequestMapping(value = "/task", method = RequestMethod.POST)
	@ApiOperation(value = "Used to post a knapsack task", notes = "", response = Void.class)
	@io.swagger.annotations.ApiResponses(value = {
			@ApiResponse(code = 200, message = "successful operation", response = Void.class) })
	public ResponseEntity<Void> postTask(@RequestBody @Valid TaskWrapper task)
			throws org.challenges.maersk.common.exceptions.ApiException {
		LOGGER.trace("[postTask] in");
		try {
			solver.solve(task);
			LOGGER.info("submitted task to solver: {}", task.getTask().getTask());
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("", e);
			throw new ApiException(e, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			LOGGER.trace("[postTask] out");
		}
	}

}
