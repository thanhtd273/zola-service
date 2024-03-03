package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
@Data
@Entity
@Table(name = "reaction")
@NamedQuery(name = "Reaction.findAll", query = "SELECT u FROM Reaction u")
public class Reaction implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long reactionId;

    @Column(name = "icon", columnDefinition = "TEXT")
    private String icon;
}
