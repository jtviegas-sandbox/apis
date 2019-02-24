package org.aprestos.labs.apis.springboot2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private String ident;

  @JsonProperty
  @NotNull
  private String text;

}
