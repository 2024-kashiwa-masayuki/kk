package com.example.kk.controller;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KkController {

    @Autowired
    TaskService taskService;
    @Autowired
    HttpSession session;

    /*
     * タスク全件表示処理
     */
    @GetMapping("/top")
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // タスクを全件取得処理
        List<TaskForm> TasksData = taskService.findAllTask();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // タスクデータオブジェクトを保管
        mav.addObject("Tasks", TasksData);

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
    public ModelAndView deleteComment(@PathVariable Integer id) {

        // 投稿をテーブルから削除
        taskService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    //新規タスク追加画面表示a
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        TaskForm tasksForm = new TaskForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", tasksForm);

        //バリデーション処理。セッションの値を取得しaddObject
        List<Object> errorList = new ArrayList<>();
        errorList.add(session.getAttribute("validationError"));
        mav.addObject("validationError", errorList);
        session.invalidate();

        return mav;
    }

    //新規タスク追加処理
    @PostMapping("/add")
    public ModelAndView addContent(@Validated  @ModelAttribute("formModel") TaskForm taskForm, BindingResult result) {
        //バリデーション処理
        if (result.hasErrors()) {
            List<String> errorList = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
            session.setAttribute("validationError", errorList);
            return new ModelAndView("redirect:/new");
        }

        // 投稿をテーブルに格納
        taskService.saveTask(taskForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    taskForm.getOpitn()

}
