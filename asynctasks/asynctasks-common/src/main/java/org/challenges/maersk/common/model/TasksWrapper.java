package org.challenges.maersk.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TasksWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("tasks")
	@JsonDeserialize(as = TasksCollection.class)
	private TasksCollection tasks;

	public TasksWrapper() {
		tasks = new TasksCollection();
	}

	public TasksCollection getTasks() {
		return tasks;
	}

	public void setTasks(TasksCollection tasks) {
		this.tasks = tasks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
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
		TasksWrapper other = (TasksWrapper) obj;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		return true;
	}

}
