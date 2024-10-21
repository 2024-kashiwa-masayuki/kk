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
     * レコード全件取得処理a
     */
    public List<TaskForm> findAllTask() {
        List<Task> results = taskRepository.findAllByOrderByUpdatedDate();
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
}
