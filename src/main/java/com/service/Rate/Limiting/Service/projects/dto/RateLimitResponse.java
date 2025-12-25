package com.service.Rate.Limiting.Service.projects.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class RateLimitResponse {
    private UUID projectId;
    private String dimension;
    private Window window;
    private Integer limitValue;
    private Instant createdAt;
    private Instant updatedAt;
}
