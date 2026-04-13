package com.example.demo.repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectId(UUID projectId);

    @Query("SELECT t from Task t WHERE t.project.id = :projectId " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)")

    List<Task> findTaskWithOptionalFilters(
            @Param("projectId") UUID projectId,
            @Param("status") TaskStatus status,
            @Param("assigneeId") UUID assigneeId
    );
}
