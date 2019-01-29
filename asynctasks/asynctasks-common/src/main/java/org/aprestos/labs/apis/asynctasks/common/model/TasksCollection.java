package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TasksCollection implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonDeserialize(contentAs = String.class)
	private final List<String> submitted;
	@JsonDeserialize(contentAs = String.class)
	private final List<String> started;
	@JsonDeserialize(contentAs = String.class)
	private final List<String> completed;

	public TasksCollection() {
    submitted = new ArrayList<String>();
    started = new ArrayList<String>();
    completed = new ArrayList<String>();
  }
	
	
	@JsonIgnore
	public static TasksCollection fromTasks(Set<Task> tasks) {
		final TasksCollection result = new TasksCollection();

		tasks.stream().forEach(task -> {
			switch (task.getStatus().getType()) {

			case completed:
				result.getCompleted().add(task.getId());
				break;
			case started:
				result.getStarted().add(task.getId());
				break;
			case submitted:
				result.getSubmitted().add(task.getId());
				break;

			}
		});

		return result;
	}



	public List<String> getSubmitted() {
    return submitted;
  }


  public List<String> getStarted() {
    return started;
  }


  public List<String> getCompleted() {
    return completed;
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
    TasksCollection other = (TasksCollection) obj;
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
