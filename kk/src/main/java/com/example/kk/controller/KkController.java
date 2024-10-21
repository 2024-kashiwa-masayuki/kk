package com.example.kk.controller;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KkController {

    @Autowired
    TaskService taskService;

    /*
     * タスク全件表示処理
     */
    @GetMapping("/top")
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
//        // タスクを全件取得処理あ
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

    /*
     * タスク絞り込み表示処理
     */
    @GetMapping("/{start}-{end}-{status}-{content}")
    public ModelAndView topDate(@RequestParam("start") String start,
                                @RequestParam("end") String end,
                                @ModelAttribute("filterCondition") FilterConditionsForm filterConditionsForm) {

        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<TaskForm> contentData = taskService.findTask(filterConditionsForm);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        // 絞り込み条件をセット
        mav.addObject("start", start);
        mav.addObject("end", end);
        return mav;
    }
    /*
     * タスク削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id){

        // 投稿をテーブルから削除
        taskService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}
