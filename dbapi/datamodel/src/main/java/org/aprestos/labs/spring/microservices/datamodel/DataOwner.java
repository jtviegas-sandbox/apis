package org.aprestos.labs.spring.microservices.datamodel;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DATA_OWNER")
public class DataOwner implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;


  @Column(name = "NAME", nullable = false, length = 30)
  private String name;

}
