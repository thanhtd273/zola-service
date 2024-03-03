package com.thanhtd.zola.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_message")
@NamedQuery(name = "UserMessage.findAll", query = "SELECT u FROM UserMessage u")
public class UserMessage implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "message_id")
    private Long messageId;
}
