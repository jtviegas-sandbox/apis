package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProblemWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Problem problem;

	public ProblemWrapper() {
	}

	public ProblemWrapper(final Problem problem) {
		this.problem = problem;
	}

	public void setProblem(final Problem problem) {
		this.problem = problem;
	}

	public Problem getProblem() {
		return problem;
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
		ProblemWrapper other = (ProblemWrapper) obj;
		if (problem == null) {
			if (other.problem != null)
				return false;
		} else if (!problem.equals(other.problem))
			return false;
		return true;
	}

}
