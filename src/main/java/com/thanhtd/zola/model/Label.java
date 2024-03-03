package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "label")
@NamedQuery(name = "Label.findAll", query = "SELECT u FROM Label u")
public class Label implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "label_id")
    private Long labelId;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;
}
