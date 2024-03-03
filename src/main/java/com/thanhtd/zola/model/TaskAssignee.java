package com.thanhtd.zola.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "task_assignee")
@NamedQuery(name = "TaskAssignee.findAll", query = "SELECT u FROM TaskAssignee u")
public class TaskAssignee implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "assignee_id")
    private Long assigneeId;
}
