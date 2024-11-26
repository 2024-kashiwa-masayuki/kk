package com.example.kk.service;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.controller.form.TaskForm;
import com.example.kk.repository.TaskRepository;
import com.example.kk.repository.entity.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    /*
     * レコード全件取得処理
     */
    public List<TaskForm> findAllTask() {
        List<Task> results = taskRepository.findAllByOrderByLimitDate();
        return setTaskForm(results);
    }
    /*
     * レコード絞り込み取得処理
     */
    public List<TaskForm> findTask(FilterConditionsForm filterConditionsForm) {
        // 取得件数の上限値
        int LIMIT_NUM = 1000;

        String start = "2020-01-01 00:00:00";
        String end = "2100-12-31 23:59:59";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            // 開始日の設定
            if (!filterConditionsForm.getStart().isBlank()) {
                start = filterConditionsForm.getStart() + " 00:00:00";
            }
            filterConditionsForm.setStartDate(formatter.parse(start));
            // 終了日の設定
            if (!filterConditionsForm.getEnd().isBlank()) {
                end = filterConditionsForm.getEnd() + " 23:59:59";
            }
            filterConditionsForm.setEndDate(formatter.parse(end));
            // 検索処理
            //DBUnitでテスト？
            if (filterConditionsForm.getContent().isBlank()) {
                if (filterConditionsForm.getStatus() == 0) {
                    // タスク内容: 指定なし、ステータス: 指定なし
                    List<Task> results = taskRepository.findByLimitDateBetween(
                            filterConditionsForm.getStartDate(),
                            filterConditionsForm.getEndDate(),
                            LIMIT_NUM);
                    return setTaskForm(results);
                } else {
                    // タスク内容: 指定なし、ステータス: 指定あり
                    List<Task> results = taskRepository.findByLimitDateBetweenByStatus(
                            filterConditionsForm.getStartDate(),
                            filterConditionsForm.getEndDate(),
                            filterConditionsForm.getStatus(),
                            LIMIT_NUM);
                    return setTaskForm(results);
                }
            } else {
                if (filterConditionsForm.getStatus() == 0) {
                    // タスク内容: 指定あり、ステータス: 指定なし
                    List<Task> results = taskRepository.findByLimitDateBetweenByContent(
                            filterConditionsForm.getStartDate(),
                            filterConditionsForm.getEndDate(),
                            filterConditionsForm.getContent(),
                            LIMIT_NUM);
                    return setTaskForm(results);
                } else {
                    // タスク内容: 指定あり、ステータス: 指定あり
                    List<Task> results = taskRepository.findByLimitDateBetweenByContentByStatus(
                            filterConditionsForm.getStartDate(),
                            filterConditionsForm.getEndDate(),
                            filterConditionsForm.getContent(),
                            filterConditionsForm.getStatus(),
                            LIMIT_NUM);
                    return setTaskForm(results);
                }
            }
        } catch (ParseException e) {
            // DateからStringへのキャストに失敗した場合は全件取得
            List<Task> results = taskRepository.findAllByOrderByUpdatedDate();
            return setTaskForm(results);
        }
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<TaskForm> setTaskForm(List<Task> results) {
        List<TaskForm> tasks = new ArrayList<>();

        for (Task result : results) {
            TaskForm task = new TaskForm();
            BeanUtils.copyProperties(result, task);
            task.setStatusLabel(task.getStatusLabel());
            tasks.add(task);
        }
        return tasks;
    }
    /*
     * レコード削除
     */
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
    /*
     * レコード追加
     */
    public void saveTask(TaskForm reqTask) {
        Task saveTask = setTasksEntity(reqTask);
        taskRepository.save(saveTask);
    }
    /*
     *リストから取得した情報をentityに設定
     */
    private Task setTasksEntity(TaskForm reqTask) {
        Task task = new Task();
        BeanUtils.copyProperties(reqTask, task);
        return task;
    }
    /*
     * レコード1件取得処理
     */
    public TaskForm editTask(Integer id) {
        List<Task> results = new ArrayList<>();
        results.add((Task) taskRepository.findById(id).orElse(null));
        List<TaskForm> tasks = setTaskForm(results);
        return tasks.get(0);
    }

    /*
     * ②レコード絞り込み取得処理の日付がblankの場合の処理テスト
     */
    public FilterConditionsForm findTaskTest(FilterConditionsForm filterConditionsForm) throws ParseException {
        // 取得件数の上限値
        int LIMIT_NUM = 1000;

        String start = "2020-01-01 00:00:00";
        String end = "2100-12-31 23:59:59";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 開始日の設定
        if (!filterConditionsForm.getStart().isBlank()) {
            start = filterConditionsForm.getStart() + " 00:00:00";
        }
        filterConditionsForm.setStartDate(formatter.parse(start));
        // 終了日の設定
        if (!filterConditionsForm.getEnd().isBlank()) {
            end = filterConditionsForm.getEnd() + " 23:59:59";
        }
        filterConditionsForm.setEndDate(formatter.parse(end));
        return filterConditionsForm;
    }
}