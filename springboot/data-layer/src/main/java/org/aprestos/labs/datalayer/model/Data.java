package org.aprestos.labs.datalayer.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "DATA")
public class Data implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long ident;

  @Column(name = "DESCRIPTION", nullable = false, length = 128)
  private String description;

  @Column(name = "TS")
  private LocalDate timestamp;

  public static Data of(String description, LocalDate timestamp) {
    Data response = new Data();
    response.setDescription(description);
    response.setTimestamp(timestamp);
    return response;
  }

  public Data() {
  }

  public Long getId() {
    return ident;
  }

  public void setId(Long id) {
    this.ident = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
    Data other = (Data) obj;
    if (ident == null) {
      if (other.ident != null)
        return false;
    } else if (!ident.equals(other.ident))
      return false;
    return true;
  }

}
