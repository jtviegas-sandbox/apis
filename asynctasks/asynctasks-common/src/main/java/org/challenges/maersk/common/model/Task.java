package org.challenges.maersk.common.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Task implements Serializable {

	@JsonIgnore
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	@NotNull
	private final String task;
	private TaskStatus status;
	private Timestamps timestamps;

	public Task() {
		this.task = Integer.toString(ID_GENERATOR.incrementAndGet());
	}

	public Task(String task) {
		this.task = task;
	}

	public String getTask() {
		return task;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Timestamps getTimestamps() {
		return timestamps;
	}

	public void setTimestamps(Timestamps timestamps) {
		this.timestamps = timestamps;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		result = prime * result + ((timestamps == null) ? 0 : timestamps.hashCode());
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
		Task other = (Task) obj;
		if (status != other.status)
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		if (timestamps == null) {
			if (other.timestamps != null)
				return false;
		} else if (!timestamps.equals(other.timestamps))
			return false;
		return true;
	}

}
