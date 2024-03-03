package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "conversation")
@NamedQuery(name = "Conversation.findAll", query = "SELECT u FROM Conversation u WHERE u.deleted = false")
public class Conversation implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long conversationId;

    @Column(name = "name")
    private String name;

    @Column(name= "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "star")
    private Boolean star;

    @Column(name = "mute_code")
    private Integer muteCode;

    @Column(name = "label_id")
    private Long labelId;

    @Column(name = "last_message_id")
    private Long lastMessageId;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "modified_date")
    private Timestamp modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;
}
