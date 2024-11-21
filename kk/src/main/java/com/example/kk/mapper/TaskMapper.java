package com.example.kk.mapper;

import com.example.kk.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface TaskMapper {
    public List<Task> findAllByOrderByUpdatedDate();

    public List<Task> findAllByOrderByLimitDate();

    public List<Task> findByLimitDateBetween(@Param("start") Date start, @Param("end") Date end, @Param("limitNum") int limitNum);

    public List<Task> findByLimitDateBetweenByStatus(@Param("start") Date start, @Param("end") Date end, @Param("status") Integer status, @Param("limitNum") int limitNum);

    public List<Task> findByLimitDateBetweenByContent(@Param("start") Date start, @Param("end") Date end, @Param("content") String content, @Param("limitNum") int limitNum);

    public List<Task> findByLimitDateBetweenByContentByStatus(@Param("start") Date start, @Param("end") Date end, @Param("content") String content, @Param("status") Integer status, @Param("limitNum") int limitNum);

    public void deleteById(@Param("id") Integer id);

    public void save(@Param("task") Task task);

    public void updateStatus(@Param("task") Task task, Integer id);

    public void updateTask(@Param("task") Task task, Integer id);

    public Task findById(@Param("id") Integer id);
}

