package org.aprestos.labs.apis.springboot2.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Task implements Serializable  {

    @Id
/*    @GeneratedValue(strategy = GenerationType.AUTO)*/
    private String ident;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TaskState> statuses = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "problem_id")
    private Problem problem;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "solution_id")
    private Solution solution;


}
