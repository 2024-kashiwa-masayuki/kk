package com.example.kk.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    @Column
    private byte status;

    @Column(name="limit_date")
    private Date limitDate;

    @Column(name="created_date", insertable = true, updatable = false)
    private Date createdDate;

    @Column(name="updated_date")
    private Date updatedDate;
}
