package com.service.Rate.Limiting.Service.projects.controller;

import com.service.Rate.Limiting.Service.auth.service.JwtService;
import com.service.Rate.Limiting.Service.common.ApiResponse;
import com.service.Rate.Limiting.Service.projects.dto.ProjectRequest;
import com.service.Rate.Limiting.Service.projects.dto.ProjectResponse;
import com.service.Rate.Limiting.Service.projects.service.ApiKeyService;
import com.service.Rate.Limiting.Service.projects.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ApiKeyService apiKeyService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProjectResponse>> create(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid ProjectRequest req
    ) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        ProjectResponse response= projectService.createProject(userId, req);
        return ResponseEntity.ok(
                ApiResponse.<ProjectResponse>builder()
                        .success(true)
                        .message("Project created successfully")
                        .data(response)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> list(
            @RequestHeader("Authorization") String authHeader
    ) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        List<ProjectResponse> response = projectService.listProjects(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<ProjectResponse>>builder()
                        .success(true)
                        .message("Projects retrieved successfully")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/{projectId}")
    public void delete(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long projectId
    ) {
        Long userId = Long.valueOf(jwtService.extractUserId(authHeader.substring(7)));
        projectService.deleteProject(projectId, userId);
    }

    @PostMapping("/{projectId}/rotate-key")
    public ResponseEntity<ApiResponse<String>> rotateKey(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long projectId
    ) {
        String key = apiKeyService.rotateKey(projectId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("API key rotated successfully")
                        .data(key)
                        .build()
        );
    }
}

