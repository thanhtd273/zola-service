package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "reply_message")
@NamedQuery(name = "ReplyMessage.findAll", query = "SELECT u FROM ReplyMessage u")
public class ReplyMessage implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reply_id")
    private Long replyId;

    @Column(name = "message_id")
    private Long messageId;
}
