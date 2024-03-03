package com.thanhtd.zola.model;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "reminder")
@NamedQuery(name = "Reminder.findAll", query = "SELECT u FROM Reminder u")
public class Reminder implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Long reminderId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "repeat_type")
    private Integer repeatType;
}
