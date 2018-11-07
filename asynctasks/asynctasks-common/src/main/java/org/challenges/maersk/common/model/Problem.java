package org.challenges.maersk.common.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Problem implements Serializable {

	private static final long serialVersionUID = 1L;
	@PositiveOrZero
	private Long capacity;
	@NotNull
	private List<@PositiveOrZero Long> weights;
	@NotNull
	private List<@PositiveOrZero Long> values;

	public Problem() {

	}

	public Problem(final Long capacity, final List<@PositiveOrZero Long> weigths,
			final List<@PositiveOrZero Long> values) {
		this.capacity = capacity;
		this.weights = weigths;
		this.values = values;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public void setWeights(List<Long> weigths) {
		this.weights = weigths;
	}

	public void setValues(List<Long> values) {
		this.values = values;
	}

	public Long getCapacity() {
		return capacity;
	}

	public List<Long> getWeights() {
		return weights;
	}

	public List<Long> getValues() {
		return values;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capacity == null) ? 0 : capacity.hashCode());
		result = prime * result + ((values == null) ? 0 : values.hashCode());
		result = prime * result + ((weights == null) ? 0 : weights.hashCode());
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
		Problem other = (Problem) obj;
		if (capacity == null) {
			if (other.capacity != null)
				return false;
		} else if (!capacity.equals(other.capacity))
			return false;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		if (weights == null) {
			if (other.weights != null)
				return false;
		} else if (!weights.equals(other.weights))
			return false;
		return true;
	}

}
