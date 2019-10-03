package org.aprestos.labs.spring.microservices.datamodel;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "DOCUMENT_TYPE")
public class DocumentType implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CODE", unique = true, nullable = false, length = 2)
  private String code;

  @Column(name = "DESCRIPTION", nullable = false, length = 60)
  private String description;

  public DocumentType() {
    super();

  }

}
