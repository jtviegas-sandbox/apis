package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Solution implements Serializable {

	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@NotNull
	private List<@Positive Double> items;
	@Positive
	private Integer time;

	public Solution() {
	}

	public Solution(final List<Double> items, final Integer time) {
		this.items = items;
		this.time = time;
	}

	public void setItems(List<Double> items) {
		this.items = items;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public List<Double> getItems() {
		return items;
	}

	public Integer getTime() {
		return time;
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
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Solution other = (Solution) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
