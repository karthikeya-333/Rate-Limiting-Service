package com.service.Rate.Limiting.Service.projects.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class RateLimitResponse {
    private Long projectId;
    private String dimension;
    private RateLimitWindow rateLimitWindow;
    private Integer limitValue;
    private Instant createdAt;
    private Instant updatedAt;
}
