package com.example.demo.service;

import com.example.demo.dto.TaskRequest;
import com.example.demo.dto.TaskUpdateRequest;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.TaskStatus;
import com.example.demo.entity.User;
import com.example.demo.exception.ForbiddenAccessException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Task createTask(UUID projectId, TaskRequest request, String requesterEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getEmail().equals(requesterEmail)) {
            throw new ForbiddenAccessException("You do not have permission to add tasks to this project");
        }

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .project(project)
                .assignee(assignee)
                .build();
        return taskRepository.save(task);
    }

    public List<Task> getTasks(UUID projectId, TaskStatus status, UUID assigneeId, String requesterEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getEmail().equals(requesterEmail)) {
            throw new ForbiddenAccessException("You do not have permission to view these tasks");
        }
        return taskRepository.findTaskWithOptionalFilters(projectId, status, assigneeId);
    }

    public Task updateTask(UUID taskId, TaskUpdateRequest request, String requesterEmail) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getProject().getOwner().getEmail().equals(requesterEmail)) {
            throw new ForbiddenAccessException("You do not have permission to update this task");
        }

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        }
        return taskRepository.save(task);
    }

    public void deleteTask(UUID taskId, String requesterEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getProject().getOwner().getEmail().equals(requesterEmail)) {
            throw new ForbiddenAccessException("You do not have permission to delete this task");
        }

        taskRepository.delete(task);
    }
}
