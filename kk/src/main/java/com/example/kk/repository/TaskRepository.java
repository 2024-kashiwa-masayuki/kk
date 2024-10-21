package com.example.kk.repository;

import com.example.kk.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    public List<Task> findAllByOrderByUpdatedDate();

    @Query(
        value = "SELECT * FROM tasks " +
                "WHERE limit_date BETWEEN :start AND :end " +
                "AND status = :status " +
                "AND content = :content",
        nativeQuery = true
    )
    public List<Task> findTask(@Param("start")Date start, @Param("end")Date end, @Param("status")String status, @Param("content")String content);
}
