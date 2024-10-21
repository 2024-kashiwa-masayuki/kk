package com.example.kk.service;

import com.example.kk.controller.form.TaskForm;
import com.example.kk.repository.TaskRepository;
import com.example.kk.repository.entity.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Task> results = taskRepository.findAllByOrderByUpdateDate();
        return setTaskForm(results);
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<TaskForm> setTaskForm(List<Task> results) {
        List<TaskForm> tasks = new ArrayList<>();

        for (Task result : results) {
            TaskForm report = new TaskForm();
            BeanUtils.copyProperties(result, report);
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
}
