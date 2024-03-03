package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "conversation_resource")
@NamedQuery(name = "ConversationResource.findAll", query = "SELECT u FROM ConversationResource u")
public class ConversationResource implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "resource_id")
    private Long resourceId;
}
