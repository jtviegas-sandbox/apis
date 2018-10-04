package org.aprestos.labs.apis.springboot.datamodel.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private String ident;

  @JsonProperty
  private String text;

  public Message() {
  }

  public Message(String text) {
    this.text = text;
  }
  
  public Message(String ident, String text) {
    this.ident = ident;
    this.text = text;
  }

  public String getId() {
    return ident;
  }

  public void setId(String id) {
    this.ident = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
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
    result = prime * result + ((ident == null) ? 0 : ident.hashCode());
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
    if (ident == null) {
      if (other.ident != null)
        return false;
    } else if (!ident.equals(other.ident))
      return false;
    if (text == null) {
      if (other.text != null)
        return false;
    } else if (!text.equals(other.text))
      return false;
    return true;
  }

}
