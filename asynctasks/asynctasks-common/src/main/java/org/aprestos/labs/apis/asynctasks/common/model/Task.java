package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Task implements Serializable {

  @JsonIgnore
  private static final long serialVersionUID = 1L;

  private String id;

  @NotNull
  private Map<String,Object> problem;

  private Map<String,Object> solution;

  private Map<Long, StatusType> statusMap;

  public Task() { 
    statusMap = new TreeMap<Long, StatusType>(new Comparator<Long>() {
    @Override
    public int compare(Long s1, Long s2) {
      return s2.compareTo(s1);
    }
  });
  }


  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  @JsonIgnore
  public void addStatus(Status status) {
    statusMap.put(status.getTimestamp(), status.getType());
    setSolution(status.getSolution());
  }
  
  @JsonIgnore
  public Status getStatus() {
    Status result = null;
    if (!statusMap.entrySet().isEmpty()) {
      Map.Entry<Long, StatusType> type = statusMap.entrySet().iterator().next();
      result = new Status(id, type.getValue(), type.getKey(), solution);
    }
    return result;
  }

  public Map<String,Object> getSolution() {
    return solution;
  }

  public void setSolution(Map<String,Object> solution) {
    this.solution = solution;
  }

  public Map<Long, StatusType> getStatusMap() {
    return statusMap;
  }

  public void setStatusMap(Map<Long, StatusType> statusMap) {
    this.statusMap = statusMap;
  }

  public Map<String,Object> getProblem() {
    return problem;
  }

  public void setProblem(Map<String, Object> problem) {
    this.problem = problem;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((problem == null) ? 0 : problem.hashCode());
    result = prime * result + ((solution == null) ? 0 : solution.hashCode());
    result = prime * result + ((statusMap == null) ? 0 : statusMap.hashCode());
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
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
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
    if (statusMap == null) {
      if (other.statusMap != null)
        return false;
    } else if (!statusMap.equals(other.statusMap))
      return false;
    return true;
  }

 
}
