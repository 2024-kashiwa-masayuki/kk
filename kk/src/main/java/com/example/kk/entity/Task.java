package com.example.kk.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class Task {
    private int id;
    private String content;
    private byte status;
    private LocalDate limitDate;
    private Date createdDate;
    private Date updatedDate;

    @PrePersist
    public  void  onPrePersist () {
        this.setStatus((byte)1);
    }

    @PreUpdate
    public  void  onPreUpdate () {
        Date date = new Date();
        this .setUpdatedDate(date);
    }
}

