package com.service.Rate.Limiting.Service.projects.service;

import com.service.Rate.Limiting.Service.projects.dto.ProjectRequest;
import com.service.Rate.Limiting.Service.projects.dto.ProjectResponse;
import com.service.Rate.Limiting.Service.projects.model.Project;
import com.service.Rate.Limiting.Service.projects.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ApiKeyService apiKeyService;

    public ProjectResponse createProject(UUID userId, ProjectRequest req) {
        Project project = Project.builder()
                .userId(userId)
                .name(req.getName())
                .description(req.getDescription())
                .build();

        Project saved = projectRepository.save(project);
        String key = apiKeyService.createApiKey(saved.getId());

        return ProjectResponse.builder()
                .name(saved.getName())
                .description(saved.getDescription())
                .createdAt(saved.getCreatedAt())
                .keyHash(key)
                .build();
    }

    public List<ProjectResponse> listProjects(UUID userId) {
        return projectRepository.findByUserIdAndIsActive(userId,Boolean.TRUE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteProject(UUID projectId, UUID userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setActive(Boolean.FALSE);
        projectRepository.save(project);
    }

    private ProjectResponse toResponse(Project p) {
        String key = apiKeyService.getApiKeyForProject(p.getId());
        return ProjectResponse.builder()
                .name(p.getName())
                .description(p.getDescription())
                .createdAt(p.getCreatedAt())
                .keyHash(key)
                .build();
    }
}

