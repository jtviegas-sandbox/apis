package org.challenges.maersk.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Timestamps implements Serializable {

	private static final long serialVersionUID = 1L;
	@NotNull
	@Positive
	private Long submitted;
	@Positive
	private Long started;
	@Positive
	private Long completed;

	public Timestamps() {
	}

	public Timestamps(final Long submitted) {
		this.submitted = submitted;
	}

	public void setSubmitted(Long submitted) {
		this.submitted = submitted;
	}

	public Long getSubmitted() {
		return submitted;
	}

	public Long getStarted() {
		return started;
	}

	public void setStarted(Long started) {
		this.started = started;
	}

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((completed == null) ? 0 : completed.hashCode());
		result = prime * result + ((started == null) ? 0 : started.hashCode());
		result = prime * result + ((submitted == null) ? 0 : submitted.hashCode());
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
		Timestamps other = (Timestamps) obj;
		if (completed == null) {
			if (other.completed != null)
				return false;
		} else if (!completed.equals(other.completed))
			return false;
		if (started == null) {
			if (other.started != null)
				return false;
		} else if (!started.equals(other.started))
			return false;
		if (submitted == null) {
			if (other.submitted != null)
				return false;
		} else if (!submitted.equals(other.submitted))
			return false;
		return true;
	}

}
