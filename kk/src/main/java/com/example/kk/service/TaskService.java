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
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    /*
     * レコード全件取得処理a
     */
    public List<TaskForm> findAllTask() {
        List<Task> results = taskRepository.findAllByOrderByUpdatedDate();
        return setTaskForm(results);
    }
    /*
     * レコード絞り込み取得処理
     */
    public List<TaskForm> findTask(FilterConditionsForm filterConditionsForm) {
        // 開始日の設定
        if (!filterConditionsForm.getStart().isBlank()) {
            filterConditionsForm.setStart(filterConditionsForm.getStart() + "00:00:00");
        } else {
            filterConditionsForm.setStart("2024-01-01 00:00:00");
        }
        // 終了日の設定
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!filterConditionsForm.getEnd().isBlank()) {
            filterConditionsForm.setEnd(filterConditionsForm.getEnd() + " 23:59:59");
        } else {
            Date endDate = new Date();
            filterConditionsForm.setEnd(formatter.format(endDate));
        }
        // ステータスの設定

        // タスクの設定
        if (filterConditionsForm.getContent().isBlank()) {
            filterConditionsForm.setContent("*");
        }
        // find
        try{
            Date startDate = formatter.parse(filterConditionsForm.getStart());
            Date endDate = formatter.parse(filterConditionsForm.getEnd());
            List<Task> results = taskRepository.findTask(
                    startDate,
                    endDate,
                    filterConditionsForm.getStatus(),
                    filterConditionsForm.getContent()
            );
            return setTaskForm(results);
        } catch (ParseException e) {
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
            TaskForm report = new TaskForm();
            BeanUtils.copyProperties(result, report);
            report.setStatusLabel(report.getStatusOption());
            tasks.add(report);
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
        task.setContent(reqTask.getContent());
        task.setStatus((byte) 1);
        task.setLimitDate(reqTask.getLimitDate());
        task.setCreatedDate(reqTask.getCreatedDate());
        task.setUpdatedDate(reqTask.getUpdatedDate());
        return task;
    }

    /*
     * レコード1件取得処理
     */
    public TaskForm editTask(Integer id) {
        List<Task> results = new ArrayList<>();
        results.add((Task) TaskRepository.findById(id));
        List<TaskForm> reports = setTaskForm(results);
        return reports.get(0);
    }
}
