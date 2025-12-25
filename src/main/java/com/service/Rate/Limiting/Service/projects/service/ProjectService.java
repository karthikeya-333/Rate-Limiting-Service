package com.service.Rate.Limiting.Service.projects.service;

import com.service.Rate.Limiting.Service.projects.dto.ProjectRequest;
import com.service.Rate.Limiting.Service.projects.dto.ProjectResponse;
import com.service.Rate.Limiting.Service.projects.model.Project;
import com.service.Rate.Limiting.Service.projects.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ApiKeyService apiKeyService;

    public ProjectResponse createProject(Long userId, ProjectRequest req) {
        Project project = Project.builder()
                .userId(userId)
                .name(req.getName())
                .description(req.getDescription())
                .build();

        Project saved = projectRepository.save(project);
        String key = apiKeyService.createApiKey(saved.getId());

        return ProjectResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .createdAt(saved.getCreatedAt())
                .keyHash(key)
                .build();
    }

    public List<ProjectResponse> listProjects(Long userId) {
        return projectRepository.findByUserIdAndActive(userId,Boolean.TRUE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        project.setActive(Boolean.FALSE);
        projectRepository.save(project);
    }

    private ProjectResponse toResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .createdAt(p.getCreatedAt())
                .build();
    }
}

