package com.example.kk.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FilterConditionsForm {
    private String start;
    private String end;
    private String status;
    private List<String> statusOption;
    private String content;
}
