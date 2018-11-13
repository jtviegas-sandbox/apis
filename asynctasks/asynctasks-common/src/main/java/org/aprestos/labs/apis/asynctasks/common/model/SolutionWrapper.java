package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SolutionWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private String task;
	@NotNull
	private Problem problem;
	@NotNull
	private Solution solution;

	public SolutionWrapper() {
	}

	public SolutionWrapper(final String task, final Problem problem, final Solution solution) {
		this.task = task;
		this.problem = problem;
		this.solution = solution;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public String getTask() {
		return task;
	}

	public Problem getProblem() {
		return problem;
	}

	public Solution getSolution() {
		return solution;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((problem == null) ? 0 : problem.hashCode());
		result = prime * result + ((solution == null) ? 0 : solution.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolutionWrapper other = (SolutionWrapper) obj;
		if (problem == null) {
			if (other.problem != null)
				return false;
		} else if (!problem.equals(other.problem))
			return false;
		if (solution == null) {
			if (other.solution != null)
				return false;
		} else if (!solution.equals(other.solution))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

}
