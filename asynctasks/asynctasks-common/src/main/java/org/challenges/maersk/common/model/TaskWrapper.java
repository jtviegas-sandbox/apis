package org.challenges.maersk.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TaskWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	private Problem problem;
	@NotNull
	private Task task;
	private Solution solution;

	@JsonIgnore
	public static TaskWrapper fromProblem(Problem problem) {
		Task task = new Task();
		task.setStatus(TaskStatus.submitted);
		Timestamps timestamps = new Timestamps(System.currentTimeMillis() / 1000);
		task.setTimestamps(timestamps);

		TaskWrapper wrapper = new TaskWrapper();
		wrapper.setTask(task);
		wrapper.setProblem(problem);

		return wrapper;
	}

	public TaskWrapper() {
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
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
		TaskWrapper other = (TaskWrapper) obj;
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
