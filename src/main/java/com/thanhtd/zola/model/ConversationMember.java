package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "conversation_member")
@NamedQuery(name = "ConversationMember.findAll", query = "SELECT u FROM ConversationMember u")
public class ConversationMember implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "role_id")
    private Long role;

    @Column(name = "last_seen_at")
    private Timestamp lastSeenAt;
}
