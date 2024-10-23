package com.example.kk.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class FilterConditionsForm {
    private String start;
    private String end;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private String content;
}
