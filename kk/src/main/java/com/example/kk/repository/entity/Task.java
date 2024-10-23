package com.example.kk.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate limitDate;

    @Column(name="created_date", insertable = false, updatable = false)
    private Date createdDate;

    @Column(name="updated_date", insertable = false, updatable = false)
    private Date updatedDate;

    @PrePersist
    public  void  onPrePersist () {
        this.setStatus((byte)1);
    }
}
