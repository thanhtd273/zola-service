package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "role")
@NamedQuery(name = "Role.findAll", query = "SELECT u FROM Role u")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "modified_date")
    private Timestamp modifiedDate;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "scope")
    private Integer scope;

    @Column(name = "deleted")
    private Boolean deleted;
}
