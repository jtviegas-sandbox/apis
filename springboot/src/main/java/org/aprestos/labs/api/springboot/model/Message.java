package org.aprestos.labs.api.springboot.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private String id;

  @JsonProperty
  private String text;

  public Message() {
  }

  public Message(String id, String text) {
    this.id = id;
    this.text = text;
  }

  protected String getId() {
    return id;
  }

  protected void setId(String id) {
    this.id = id;
  }

  protected String getText() {
    return text;
  }

  protected void setText(String text) {
    this.text = text;
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
    result = prime * result + ((text == null) ? 0 : text.hashCode());
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
    Message other = (Message) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (text == null) {
      if (other.text != null)
        return false;
    } else if (!text.equals(other.text))
      return false;
    return true;
  }

}
