package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "action")
@NamedQuery(name = "Action.findAll", query = "SELECT u FROM Action u")
public class Action implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long actionId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private Integer code;
}
