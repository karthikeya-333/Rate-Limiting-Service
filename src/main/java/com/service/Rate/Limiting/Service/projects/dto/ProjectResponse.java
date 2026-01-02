package com.service.Rate.Limiting.Service.projects.dto;


import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String keyHash;
    private String name;
    private String description;
    private Instant createdAt;
}

