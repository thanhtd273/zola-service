package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "contact")
@NamedQuery(name = "Contact.findAll", query = "SELECT u FROM Contact u")
public class Contact implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "friend_id")
    private Long friendId;

    @Column(name = "contact_name")
    private String contactName;
}
