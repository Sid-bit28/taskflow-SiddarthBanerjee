package com.example.demo.dto;

import com.example.demo.entity.TaskPriority;
import com.example.demo.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private UUID assigneeId;
}
