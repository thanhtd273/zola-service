package com.thanhtd.zola.model;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "permission")
@NamedQuery(name = "Permission.findAll", query = "SELECT u FROM Permission u")
public class Permission implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "name")
    private String name;

    @Column(name = "action_ids")
    private Integer actionIds;

    @Column(name = "deleted")
    private Boolean deleted;
}
