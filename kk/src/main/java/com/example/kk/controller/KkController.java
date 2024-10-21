package com.example.kk.controller;

import com.example.kk.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class KkController {

    @Autowired
    TaskService taskService;

    @GetMapping("/top")
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
//        // タスクを全件取得処理
//        List<ReportForm> contentData = reportService.findAllReport();
        // 画面遷移先を指定
        mav.setViewName("/top");
//        // タスクデータオブジェクトを保管
//        mav.addObject("contents", contentData);

        //本日の日付の取得
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        String today = sdFormat.format(date);
        mav.addObject("today", today);
        return mav;
    }
}
