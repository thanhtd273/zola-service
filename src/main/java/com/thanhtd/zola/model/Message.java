package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "message")
@NamedQuery(name = "Message.findAll", query = "SELECT u FROM Message u WHERE deleted = false")
public class Message implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "modified_date")
    private Timestamp modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;
}
