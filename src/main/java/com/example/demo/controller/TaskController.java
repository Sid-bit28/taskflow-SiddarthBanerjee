package com.example.demo.controller;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskUpdateRequest;
import com.example.demo.entity.Task;
import com.example.demo.entity.TaskStatus;
import com.example.demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(
            @PathVariable UUID projectId,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Task task = taskService.createTask(projectId, request, email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(task);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getTasks(
            @PathVariable UUID projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID assigneeId,
            Authentication authentication) {

        String email = authentication.getName();

        List<Task> tasks = taskService.getTasks(projectId, status, assigneeId, email);

        return ResponseEntity.ok(tasks);
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskUpdateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        Task updatedTask = taskService.updateTask(taskId, request, email);

        return ResponseEntity.ok(updatedTask);
    }
}

// eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaWRkYXJ0aEBleGFtcGxlLmNvbSIsInVzZXJfaWQiOiI1ZTZlZTM5MC02ZjIyLTQwMTUtYmM2MC0xM2Y0MGExNzcyNGUiLCJpYXQiOjE3NzYxMzgxNjIsImV4cCI6MTc3NjIyNDU2Mn0.DMCoADeAqW9edZSGelMuoNImThDjdu8GKy0A1lJHyklst8KEj7nYC0Y2QT0unHsX6sNfTILsMWU4hYd9BN9vIQ