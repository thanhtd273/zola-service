package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "message_resource")
@NamedQuery(name = "MessageResource.findAll", query = "SELECT u FROM MessageResource u")
public class MessageResource implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "message_id")
    private Long messageId;
}
