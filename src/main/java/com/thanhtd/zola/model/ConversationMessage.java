package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "conversation_message")
@NamedQuery(name = "ConversationMessage.findAll", query = "SELECT u FROM ConversationMessage u")
public class ConversationMessage implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "star")
    private Boolean star;
}
