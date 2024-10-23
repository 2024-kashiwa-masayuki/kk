package com.example.kk.controller;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.repository.entity.Task;
import com.example.kk.service.TaskService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
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
        mav.addObject("tasks", TasksData);

        //本日の日付の取得
        Date date = new Date();
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        String today = sdFormat.format(date);
        mav.addObject("today", today);
        //空のオブジェクトをセット
        FilterConditionsForm filterConditionsForm = new FilterConditionsForm();
        filterConditionsForm.setContent("");
        mav.addObject("filterConditionsForm", filterConditionsForm);
        // ステータスマップをセット
        TaskForm taskForm = new TaskForm();
        mav.addObject("taskForm", taskForm);
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
        // タスクを取得
        List<TaskForm> TasksData  = taskService.findTask(filterConditionsForm);
        // タスクデータオブジェクトを保管
        mav.addObject("tasks", TasksData);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // オブジェクトをセット
        mav.addObject("filterConditionsForm", filterConditionsForm);
        // ステータスマップをセット
        TaskForm taskForm = new TaskForm();
        mav.addObject("taskForm", taskForm);
        return mav;
    }
    /*
     * タスク削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteTask(@PathVariable Integer id) {

        // 投稿をテーブルから削除
        taskService.deleteTask(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/top");
    }
    /*
     * タスクステータス変更処理
     */
    @GetMapping("/change/{id}-{status}")
    public ModelAndView changeStatus(@PathVariable Integer id,
                                     @ModelAttribute("taskForm") TaskForm reqTaskForm) {

        // ステータスを変更
        TaskForm taskForm = taskService.editTask(id);
        taskForm.setStatus(reqTaskForm.getStatus());
        taskService.saveTask(taskForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/top");
    }
    //新規タスク追加画面表示
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        TaskForm tasksForm = new TaskForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", tasksForm);
        return mav;
    }

    //新規タスク追加処理
    @PostMapping("/add")
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") TaskForm taskForm, BindingResult result) {
        ModelAndView mav = new ModelAndView();
        //バリデーション処理
        List<String> errorList = new ArrayList<String>();

        //全角スペースもバリデーションでひっかけるための処理
        if (StringUtils.isBlank(taskForm.getContent())) {
            errorList.add("タスクを入力してください");
        }

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
        }

        if (errorList.size() != 0) {
            mav.addObject("validationError", errorList);
            mav.setViewName("/new");
            return mav;
        }

        // 投稿をテーブルに格納
        taskService.saveTask(taskForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/top");
    }

    /*
     * タスク編集画面表示
     */
    @GetMapping("/edit/{strId}")
    public ModelAndView editContent(@PathVariable String strId,
                                    RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        //バリデーション処理
        if (!strId.matches("^[0-9]+$")) {
            List<String> errorList = new ArrayList<String>();
            errorList.add("不正なパラメータです");
            redirectAttributes.addFlashAttribute("validationError", errorList);
            mav.setViewName("redirect:/top");
            return mav;
        }

        int id = Integer.parseInt(strId);
        //編集するタスクを取得。
        TaskForm task = taskService.editTask(id);
        //編集するタスクをセット
        mav.addObject("formModel", task);
        //画面遷移先を指定
        mav.setViewName("/edit");

        return mav;
    }

    /*
     * タスク編集処理
     */
    @PostMapping("/edit/{id}")
    public ModelAndView updateContent(@PathVariable Integer id, @Validated @ModelAttribute("formModel") TaskForm taskForm, BindingResult result) throws ParseException {
        ModelAndView mav = new ModelAndView();

        //バリデーション処理
        List<String> errorList = new ArrayList<String>();

        //全角スペースもバリデーションでひっかけるための処理
        if (StringUtils.isBlank(taskForm.getContent())) {
            errorList.add("タスクを入力してください");
        }

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
        }

        if (errorList.size() != 0) {
            mav.addObject("validationError", errorList);
            mav.setViewName("/edit");
            return mav;
        }



        // 投稿をテーブルに格納
        taskService.saveTask(taskForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/top");
    }
}