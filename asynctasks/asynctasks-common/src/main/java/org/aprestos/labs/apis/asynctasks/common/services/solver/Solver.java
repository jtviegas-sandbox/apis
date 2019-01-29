package org.aprestos.labs.apis.asynctasks.common.services.solver;

import java.util.Map;

public interface Solver {

	String submitProblem(Map<String,Object> problem) throws SolverException;

}