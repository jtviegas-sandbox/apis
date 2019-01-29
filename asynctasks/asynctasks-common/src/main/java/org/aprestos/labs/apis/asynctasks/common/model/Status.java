package org.aprestos.labs.apis.asynctasks.common.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Status implements Serializable{
  
  private final String id;
  private final StatusType type;
  private final Long timestamp;
  private Map<String,Object> solution;
  
  public Status(final String id, final StatusType type, final Long timestamp) {
    this.id = id;
    this.type = type;
    this.timestamp = timestamp;
  }
  
  public Status(final String id, final StatusType type, final Long timestamp, final Map<String,Object> solution) {
    this.id = id;
    this.type = type;
    this.timestamp = timestamp;
    this.solution = solution;
  }
  
  @JsonIgnore
  public static Status create(final String id, final StatusType type, final Long timestamp) {
    return new Status(id, type, timestamp);
  }
  
  @JsonIgnore
  public static Status create(final String id, final StatusType type, final Long timestamp, final Map<String,Object> solution) {
    return new Status(id, type, timestamp, solution);
  }
  
  public Map<String,Object> getSolution() {
    return solution;
  }


  public String getId() {
    return id;
  }


  public StatusType getType() {
    return type;
  }


  public Long getTimestamp() {
    return timestamp;
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
    result = prime * result + ((solution == null) ? 0 : solution.hashCode());
    result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    Status other = (Status) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (solution == null) {
      if (other.solution != null)
        return false;
    } else if (!solution.equals(other.solution))
      return false;
    if (timestamp == null) {
      if (other.timestamp != null)
        return false;
    } else if (!timestamp.equals(other.timestamp))
      return false;
    if (type != other.type)
      return false;
    return true;
  }


 
  
  

}
