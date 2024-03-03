package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "message_reaction")
@NamedQuery(name = "MessageReaction.findAll", query = "SELECT u FROM MessageReaction u")
public class MessageReaction implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reaction_id")
    private Long reactionId;

    @Column(name = "message_id")
    private Long messageId;
}
