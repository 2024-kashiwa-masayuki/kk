package com.example.kk.repository;

import com.example.kk.controller.form.FilterConditionsForm;
import com.example.kk.repository.entity.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    public List<Task> findAllByOrderByUpdatedDate();
    public List<Task> findAllByOrderByLimitDate();

    @Query(
        value = "SELECT * FROM tasks " +
                "WHERE limit_date BETWEEN :start AND :end " +
                "ORDER BY limit_date ASC LIMIT :limitNum",
        nativeQuery = true
    )
    public List<Task> findByLimitDateBetween(@Param("start")Date start, @Param("end")Date end, @Param("limitNum")int limitNum);

    @Query(
            value = "SELECT * FROM tasks " +
                    "WHERE limit_date BETWEEN :start AND :end " +
                    "AND status = :status " +
                    "ORDER BY limit_date ASC LIMIT :limitNum",
            nativeQuery = true
    )
    public List<Task> findByLimitDateBetweenByStatus(@Param("start")Date start, @Param("end")Date end, @Param("status")Integer status, @Param("limitNum")int limitNum);

    @Query(
            value = "SELECT * FROM tasks " +
                    "WHERE limit_date BETWEEN :start AND :end " +
                    "AND content = :content " +
                    "ORDER BY limit_date ASC LIMIT :limitNum",
            nativeQuery = true
    )
    public List<Task> findByLimitDateBetweenByContent(@Param("start")Date start, @Param("end")Date end, @Param("content")String content, @Param("limitNum")int limitNum);

    @Query(
            value = "SELECT * FROM tasks " +
                    "WHERE limit_date BETWEEN :start AND :end " +
                    "AND content = :content " +
                    "AND status = :status " +
                    "ORDER BY limit_date ASC LIMIT :limitNum",
            nativeQuery = true
    )
    public List<Task> findByLimitDateBetweenByContentByStatus(@Param("start")Date start, @Param("end")Date end, @Param("content")String content, @Param("status")Integer status, @Param("limitNum")int limitNum);
}
