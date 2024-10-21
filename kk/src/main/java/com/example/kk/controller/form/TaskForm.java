package com.example.kk.controller.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskForm {

    private int id;

    @NotEmpty
    private String content;

    private byte status;

    private Date limitDate;

    private Date createdDate;

    private Date updatedDate;
}