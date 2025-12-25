package com.service.Rate.Limiting.Service.projects.service;

import com.service.Rate.Limiting.Service.projects.dto.RateLimitRegisterRequest;
import com.service.Rate.Limiting.Service.projects.dto.RateLimitResponse;
import com.service.Rate.Limiting.Service.projects.model.Project;
import com.service.Rate.Limiting.Service.projects.model.RateLimit;
import com.service.Rate.Limiting.Service.projects.repository.ProjectRepository;
import com.service.Rate.Limiting.Service.projects.repository.RateLimitRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RateLimitRepository rateLimitRepository;
    private final ProjectRepository projectRepository;

    public RateLimitResponse createOrUpdateLimit(RateLimitRegisterRequest req, UUID userId) {
        UUID projectId = req.getProjectId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Project not found for this user");
        }

        Optional<RateLimit> existing = rateLimitRepository
                .findByProjectIdAndDimensionAndWindow(req.getProjectId(), req.getDimension(), req.getWindow());

        RateLimit limit;
        if (existing.isPresent()) {
            limit = existing.get();
            limit.setLimitValue(req.getLimitValue());
            limit.setUpdatedAt(Instant.now());
        } else {
            limit = RateLimit.builder().
                    projectId(req.getProjectId())
                    .dimension(req.getDimension())
                    .window(req.getWindow())
                    .limitValue(req.getLimitValue())
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now()).build();
        }

        return mapToDTO(rateLimitRepository.save(limit));
    }

    public List<RateLimitResponse> getLimitsForProject(UUID projectId, UUID userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        if (!project.getUserId().equals(userId)) {
            throw new EntityNotFoundException("Project not found for this user");
        }

        List<RateLimit> limits= rateLimitRepository.findByProjectId(projectId);

        return limits.stream().map(this::mapToDTO).toList();
    }

    public void deleteLimit(UUID limitId) {
        rateLimitRepository.deleteById(limitId);
    }

    private RateLimitResponse mapToDTO(RateLimit limit) {
        return RateLimitResponse.builder()
                .projectId(limit.getProjectId())
                .dimension(limit.getDimension())
                .window(limit.getWindow())
                .limitValue(limit.getLimitValue())
                .createdAt(limit.getCreatedAt())
                .updatedAt(limit.getUpdatedAt())
                .build();
    }
}

