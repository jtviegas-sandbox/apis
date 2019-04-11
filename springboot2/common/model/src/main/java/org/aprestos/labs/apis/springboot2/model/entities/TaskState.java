package org.aprestos.labs.apis.springboot2.model.entities;

import lombok.Data;
import org.aprestos.labs.apis.springboot2.model.dto.TaskStatus;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class TaskState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ident;
    @Column(nullable = false)
    private Long timestamp;
    @Column(nullable = false)
    private TaskStatus status;

}
