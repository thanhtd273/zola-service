package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "resource")
@NamedQuery(name = "Resource.findAll", query = "SELECT u FROM Resource u")
public class Resource implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "path", columnDefinition = "TEXT")
    private String path;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "scope")
    private Integer scope;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "size")
    private Long size;

    @Column(name = "create_date")
    private Timestamp createDate;
}
