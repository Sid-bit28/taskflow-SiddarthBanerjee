package com.example.demo.repository;

import com.example.demo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN p.tasks t WHERE p.owner.email = :email OR t.assignee.email = :email")
    List<Project> findProjectsByOwnerOrTaskAssignee(@Param("email") String email);
}
