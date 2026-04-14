package com.example.demo.controller;

import com.example.demo.dto.ProjectRequest;
import com.example.demo.entity.Project;
import com.example.demo.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request, Authentication authentication) {
        String ownerEmail = authentication.getName();

        Project project = projectService
                .createProject(request.getName(), request.getDescription(), ownerEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getUserProjects(Authentication authentication) {
        String ownerEmail = authentication.getName();
        List<Project> projects = projectService.getProjectsByUser(ownerEmail);
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable UUID id,
            Authentication authentication) {

        String ownerEmail = authentication.getName();
        projectService.deleteProjectById(id, ownerEmail);
        return ResponseEntity.noContent().build();
    }
}
