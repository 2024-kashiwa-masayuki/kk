package com.example.kk.controller;

import com.example.kk.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class KkController {

    @Autowired
    TaskService taskService;
}
