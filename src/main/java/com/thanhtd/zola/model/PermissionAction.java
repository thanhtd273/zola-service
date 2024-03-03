package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "permission_action")
@NamedQuery(name = "PermissionAction.findAll", query = "SELECT u FROM PermissionAction u")
public class PermissionAction implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "action_id")
    private Long actionId;
}
