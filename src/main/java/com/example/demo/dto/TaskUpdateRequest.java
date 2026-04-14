package com.example.demo.dto;

import com.example.demo.entity.TaskStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class TaskUpdateRequest {
    private TaskStatus status;
    private UUID assigneeId;
}
