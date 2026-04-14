package com.example.demo.service;

import com.example.demo.dto.ProjectUpdateRequest;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public Project createProject(String name, String description, String ownerEmail) {
        User user = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Project project = Project.builder()
                .name(name)
                .description(description)
                .owner(user)
                .build();
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByUser(String ownerEmail) {
        return projectRepository.findProjectsByOwnerOrTaskAssignee(ownerEmail);
    }

    public void deleteProjectById(UUID projectId, String ownerEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found."));
        if(!project.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("You do not have permission to delete this project");
        }
        projectRepository.delete(project);
    }

    public Project getProjectById(UUID projectId, String requesterEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        if (!project.getOwner().getEmail().equals(requesterEmail)) {
            throw new RuntimeException("You do not have permission to view this project");
        }
        return project;
    }

    public Project updateProject(UUID projectId, ProjectUpdateRequest request, String requesterEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        if (!project.getOwner().getEmail().equals(requesterEmail)) {
            throw new RuntimeException("You do not have permission to update this project");
        }
        if (request.getName() != null) {
            project.setName(request.getName());
        }

        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        return projectRepository.save(project);
    }
}
